package com.WealthTracker.demo.domain.expend.service;

import com.WealthTracker.demo.domain.category.repository.ExpendCategoryRepository;
import com.WealthTracker.demo.domain.expend.dto.ExpendRequestDTO;
import com.WealthTracker.demo.domain.expend.entity.CategoryExpend;
import com.WealthTracker.demo.domain.expend.repository.ExpendRepository;
import com.WealthTracker.demo.domain.user.entity.User;
import com.WealthTracker.demo.domain.user.repository.UserRepository;
import com.WealthTracker.demo.global.util.JwtUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ExpendServiceImplTest {
    @Autowired
    private ExpendServiceImpl expendService;

    @Autowired
    private ExpendCategoryRepository expendCategoryRepository;

    @Autowired
    private ExpendRepository expendRepository;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private JwtUtil jwtUtil;


    final String mockToken="Bearer Test token";

    @BeforeEach
    @Transactional
    void setUp(){
        expendRepository.deleteAll();
        userRepository.deleteAll();

        User testUser=User.builder()
                .email("test@naver.com")
                .name("test")
                .nickName("test")
                .password("test")
                .build();
        testUser=userRepository.save(testUser);

        when(jwtUtil.validationToken(mockToken))
                .thenReturn(true);
        when(jwtUtil.getUserId(mockToken))
                .thenReturn(testUser.getUserId());
    }

    /**
     * 시나리오
     * 동시에 같은 카테고리 지출 생성 요청 -> 여러 요청이 모두 카테고리가 존재하지 않는다고 판단하여 중복하여 생성 가능성.
     */

    @Test
    @DisplayName("동시에 100개의 같은 카테고리를 가진 지출을 저장한다.")
    void saveExpend_100_request() throws InterruptedException {
        //given
        final int threadCount=10;
        final ExecutorService executorService= Executors.newFixedThreadPool(32);
        final CountDownLatch countDownLatch=new CountDownLatch(threadCount);

        //when
        for(int i=0;i<threadCount;i++){
            executorService.submit(()->{
                try {
                    ExpendRequestDTO expendRequestDTO=createMockExpendRequestDTO();
                    Long savedExpendId=expendService.writeExpend(expendRequestDTO,mockToken);

                }catch (Exception e){
                    e.printStackTrace();
                }

                finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        executorService.shutdown();

        //then
        //실제 생성된 카테고리
        List<CategoryExpend> allCategory=expendCategoryRepository.findAll();
        long count=allCategory.stream()
                .filter(c->c.getCustomCategoryName()!=null)
                        .filter(c->c.getCustomCategoryName().equalsIgnoreCase("TEST"))
                                .count();

        assertThat(count)
                .isEqualTo(1);
    }

    /**
     * ExpendRequestDTO 생성
     */
    private ExpendRequestDTO createMockExpendRequestDTO(){
        return ExpendRequestDTO
                .builder()
                .asset("현금")
                .category("TEST")
                .expendDate("2022-12-22")
                .cost(100000L)
                .expendName("테스트를 위한 지출 이름")
                .build();
    }

}