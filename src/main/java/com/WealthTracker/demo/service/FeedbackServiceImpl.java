package com.WealthTracker.demo.service;

import com.WealthTracker.demo.DTO.feedback.ChatRequest;
import com.WealthTracker.demo.DTO.feedback.ChatResponse;
import com.WealthTracker.demo.constants.ErrorCode;
import com.WealthTracker.demo.domain.User;
import com.WealthTracker.demo.enums.Category_Expend;
import com.WealthTracker.demo.error.CustomException;
import com.WealthTracker.demo.repository.ExpendRepository;
import com.WealthTracker.demo.repository.UserRepository;
import com.WealthTracker.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService{

    @Autowired
    @Qualifier("geminiRestTemplate")
    private RestTemplate template;

    private final UserRepository userRepository;
    private final ExpendRepository expendRepository;
    private final JwtUtil jwtUtil;

    @Override
    public String generateMessage(User user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime mondayStart = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toLocalDate().atStartOfDay();
        LocalDateTime sundayEnd = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).toLocalDate().atTime(23, 59, 59);

        //유저의 이번 달 지출 목록 가져오기
        //납부 카테고리 이번 달 총합
        Long paymentCategorySum=expendRepository.amountByCategory(user, Category_Expend.PAYMENT, mondayStart, sundayEnd);

        //식비 카테고리 이번 달 총합
        Long foodCategorySum=expendRepository.amountByCategory(user,Category_Expend.FOOD, mondayStart, sundayEnd);

        //교통 카테고리 이번 달 총합
        Long transportationSum=expendRepository.amountByCategory(user,Category_Expend.TRANSPORTATION, mondayStart, sundayEnd);

        //오락 카테고리 이번 달 총합
        Long gameSum=expendRepository.amountByCategory(user,Category_Expend.GAME, mondayStart, sundayEnd);

        //쇼핑 카테고리 이번 달 총합
        Long shoppingSum=expendRepository.amountByCategory(user,Category_Expend.SHOPPING, mondayStart, sundayEnd);

        //기타 카테고리 이번 달 총합
        Long etcSum=expendRepository.amountByCategory(user,Category_Expend.ETC, mondayStart, sundayEnd);

        //메세지만들기
        return "이번 주 지출 내역은 납부는 "+paymentCategorySum+"원 ,"+
                "식비 지출은 "+foodCategorySum+"원 ,"+
                "교통비 지출은 "+transportationSum+"원 ,"+
                "오락비 지출은 "+gameSum+"원 ,"+
                "쇼핑비 지출은 "+shoppingSum+"원 ,"+
                "기타 지출은 "+etcSum+"원이야 이번 주 총 지출금액, 카테고리별 지출금액도 보여주고 지출내역에 대해 자세한 충고해줘,나열식이 아니라 한 문단으로 요약,%로 분석";
    }

    @Override
    public String sendFeedBack(String token,String key,String url) {
        String requestUrl=url+"?key="+key;
        //유저 찾기
        Optional<User> user = userRepository.findByUserId(jwtUtil.getUserId(token));
        User myUser=user.orElseThrow(
                ()->new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        String prompt=generateMessage(myUser);

        ChatRequest request=new ChatRequest(prompt);
        ChatResponse response=template.postForObject(requestUrl,request, ChatResponse.class);
        return response.getCandidates().get(0).getContent().getParts().get(0).getText().toString();
    }
}
