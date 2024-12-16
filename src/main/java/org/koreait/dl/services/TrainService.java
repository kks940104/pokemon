package org.koreait.dl.services;

import lombok.RequiredArgsConstructor;
import org.koreait.dl.entities.QTrainItem;
import org.koreait.dl.entities.TrainItem;
import org.koreait.dl.repositoris.TrainItemRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.data.domain.Sort.Order.asc;


/**
 * 여기 Service는 데이터를 학습하는곳
 */

@Lazy
@Profile("dl")
@Service
@RequiredArgsConstructor
public class TrainService {

    private final TrainItemRepository repository;

    @Value("${python.run.path}")
    private String runPath;

    @Value("${python.script.path}")
    private String scriptPath;

    @Value("${python.data.url}")
    private String dataUrl;

    // @Scheduled(cron="0 0 1 * * *") // 새벽 1시 마다 훈련
    public void process() {
        try {
            // region 기존 경로

            /*
            ProcessBuilder builder = new ProcessBuilder("C:\\Users\\admin\\AppData\\Local\\Programs\\Python\\Python39\\python.exe", "D:/recommend/train.py", "http://localhost:3000/api/dl/data");
            */

            // endregion

            ProcessBuilder builder = new ProcessBuilder(runPath, scriptPath + "train.py", dataUrl + "?mode=ALL", dataUrl); // 매개변수 2개, 1 : 전체데이터, 2: 학습데이터
            Process process = builder.start();
            int exitCode = process.waitFor(); // 디버깅중 문제가 있다면 이거 확인해보자.
            System.out.println(exitCode);

        } catch (Exception e) {}
    }

    public void log(TrainItem item) {
        repository.saveAndFlush(item);
    }

    public List<TrainItem> getList(boolean isAll) {
        if (isAll) {
            return repository.findAll(Sort.by(asc("createdAt")));
        } else {
            QTrainItem trainItem = QTrainItem.trainItem;
            return (List<TrainItem>) repository.findAll(trainItem.createdAt.after(LocalDateTime.of(LocalDate.now().minusDays(1L), LocalTime.of(0,0,0))), Sort.by(asc("createdAt")));
        }
    }
}












