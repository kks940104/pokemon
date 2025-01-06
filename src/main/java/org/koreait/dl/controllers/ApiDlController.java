package org.koreait.dl.controllers;

import lombok.RequiredArgsConstructor;
import org.koreait.dl.entities.TrainItem;
import org.koreait.dl.services.PredictService;
import org.koreait.dl.services.SentimentService;
import org.koreait.dl.services.TrainService;
import org.koreait.global.rests.JSONData;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Profile("dl")
@RestController
@RequestMapping("/api/dl")
@RequiredArgsConstructor
public class ApiDlController {

    private final PredictService predictService;
    private final TrainService trainService;
    private final SentimentService sentimentService;

    @GetMapping("/data")
    public List<TrainItem> sendData(@RequestParam(name="mode", required = false) String mode) {
        List<TrainItem> items = trainService.getList(mode != null && mode.equals("ALL"));
        return items;

        // region 이전 예제 코드

/*        Random random = new Random();
        List<TrainItem> items = IntStream.range(0, 1000)
                .mapToObj(i -> TrainItem.builder()
                        .items1(random.nextInt())
                        .items2(random.nextInt())
                        .items3(random.nextInt())
                        .items4(random.nextInt())
                        .items5(random.nextInt())
                        .items6(random.nextInt())
                        .items7(random.nextInt())
                        .items8(random.nextInt())
                        .items9(random.nextInt())
                        .items10(random.nextInt())
                        .result(random.nextInt(4))
                        .build()
                ).toList();

        return items;*/

        // endregion
    }

    @PostMapping("/predict")
    public JSONData predict(@RequestParam("items") List<int[]> items) {
        int[] predictions = predictService.predict(items);

        return new JSONData(predictions);
    }

    @PostMapping("/sentiment")
    public JSONData sentiment(@RequestParam("items") List<String> items) {
        double[] predictions = sentimentService.predict(items);

        return new JSONData(predictions);
    }
}