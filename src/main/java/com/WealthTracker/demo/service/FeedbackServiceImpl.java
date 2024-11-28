package com.WealthTracker.demo.service;

import com.WealthTracker.demo.DTO.feedback.ChatRequest;
import com.WealthTracker.demo.DTO.feedback.ChatResponse;
import com.WealthTracker.demo.constants.ErrorCode;
import com.WealthTracker.demo.domain.FeedBack;
import com.WealthTracker.demo.domain.User;
import com.WealthTracker.demo.enums.Category_Expend;
import com.WealthTracker.demo.error.CustomException;
import com.WealthTracker.demo.repository.ExpendRepository;
import com.WealthTracker.demo.repository.FeedbackRepository;
import com.WealthTracker.demo.repository.UserRepository;
import com.WealthTracker.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    @Qualifier("geminiRestTemplate")
    private RestTemplate template;

    private final UserRepository userRepository;
    private final ExpendRepository expendRepository;
    private final FeedbackRepository feedbackRepository;
    private final JwtUtil jwtUtil;

    @Override
    public String generateMessage(User user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime mondayStart = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toLocalDate().atStartOfDay();
        LocalDateTime sundayEnd = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).toLocalDate().atTime(23, 59, 59);

        //유저의 이번 달 지출 목록 가져오기
        //납부 카테고리 이번 달 총합
        Long paymentCategorySum = expendRepository.amountByCategory(user, Category_Expend.PAYMENT, mondayStart, sundayEnd);

        //식비 카테고리 이번 달 총합
        Long foodCategorySum = expendRepository.amountByCategory(user, Category_Expend.FOOD, mondayStart, sundayEnd);

        //교통 카테고리 이번 달 총합
        Long transportationSum = expendRepository.amountByCategory(user, Category_Expend.TRANSPORTATION, mondayStart, sundayEnd);

        //오락 카테고리 이번 달 총합
        Long gameSum = expendRepository.amountByCategory(user, Category_Expend.GAME, mondayStart, sundayEnd);

        //쇼핑 카테고리 이번 달 총합
        Long shoppingSum = expendRepository.amountByCategory(user, Category_Expend.SHOPPING, mondayStart, sundayEnd);

        //기타 카테고리 이번 달 총합
        Long etcSum = expendRepository.amountByCategory(user, Category_Expend.ETC, mondayStart, sundayEnd);

        //메세지만들기
        String message = "이번 주 지출 내역은 납부는 " + paymentCategorySum + "원 ," +
                "식비 지출은 " + foodCategorySum + "원 ," +
                "교통비 지출은 " + transportationSum + "원 ," +
                "오락비 지출은 " + gameSum + "원 ," +
                "쇼핑비 지출은 " + shoppingSum + "원 ," +
                "기타 지출은 " + etcSum + "원이야 이번 주 총 지출금액, 카테고리별 지출금액도 보여주고 지출내역에 대해 자세한 충고해줘,나열식이 아니라 한 문단으로 요약,%로 분석";

        log.info(message);
        return message;
    }

    @Override
    @Transactional
    public String sendFeedBack(String token, String key, String url) {
        String requestUrl = url + "?key=" + key;
        //유저 찾기
        Optional<User> user = userRepository.findByUserId(jwtUtil.getUserId(token));
        User myUser = user.orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        //최신 지출 날짜
        LocalDateTime latestExpendDate = expendRepository.findLatestExpend(myUser);

        //최신 지출 수정 날짜
        LocalDateTime latestUpdateExpendDate = expendRepository.findLatestUpdateDate(myUser);
        log.info(latestUpdateExpendDate.toString());
        //현재 주의 시작과 끝 날짜 계산
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekStart = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toLocalDate().atStartOfDay();
        LocalDateTime weekEnd = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).toLocalDate().atTime(23, 59, 59);

        //피드백 DB에 없을 때만 GEMINI API 호출
        Optional<FeedBack> existingFeedback = feedbackRepository.findFirstByCreatedAtBetweenOrderByCreatedAtDesc(weekStart, weekEnd);
        if (existingFeedback.isPresent()) {
            //지출 내역 업데이트 유무 확인
            LocalDateTime feedbackCreationDate = existingFeedback.get().getCreatedAt();

            // 새로운 지출이 있거나 지출이 수정된 경우 새로운 피드백 생성
            if ((latestExpendDate != null && latestExpendDate.isAfter(feedbackCreationDate)) ||
                    (latestUpdateExpendDate != null && latestUpdateExpendDate.isAfter(feedbackCreationDate))) {
                return generateAndSaveFeedback(myUser, requestUrl);
            }

            // 변경사항이 없는 경우 기존 피드백 반환
            return existingFeedback.get().getContent();
        }
        //새로운 피드백 생성
        return generateAndSaveFeedback(myUser, requestUrl);
    }

    //피드백 생성 및 저장
    private String generateAndSaveFeedback(User user, String requestUrl) {
        String prompt = generateMessage(user);
        ChatRequest request = new ChatRequest(prompt);
        ChatResponse response = template.postForObject(requestUrl, request, ChatResponse.class);
        String feedback = response.getCandidates().get(0).getContent().getParts().get(0).getText().toString().replaceAll("[\\n*]", "");

        //새 피드백 저장.
        FeedBack feedBack = FeedBack.builder()
                .content(feedback)
                .build();
        //유저의 피드백 저장

        feedbackRepository.save(feedBack);

        //유저에 피드백 저장
        user.setFeedBack(feedBack);
        userRepository.save(user);

        return feedback;
    }

    //매주 일요일 스켸쥴러
    @Scheduled(cron = "0 0 0 * * SUN")
    @Transactional
    public void cleanAndSaveFeedback() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekStart = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toLocalDate().atStartOfDay();

        //이번 주 피드백 삭제
        feedbackRepository.deleteByCreatedAtBefore(weekStart);
    }
}
