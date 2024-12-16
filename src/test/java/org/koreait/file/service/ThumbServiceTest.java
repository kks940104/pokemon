package org.koreait.file.service;

import org.junit.jupiter.api.Test;
import org.koreait.file.controllers.RequestThumb;
import org.koreait.file.services.ThumbnailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ThumbServiceTest {

    @Autowired
    private ThumbnailService service;


    @Test
    void thumbPathTest() {
        RequestThumb form = new RequestThumb();
        form.setSeq(2L);
        form.setWidth(100);
        form.setHeight(100);
        String path = service.getThumbPath(2L,null,100,100);
        path = service.create(form);
        System.out.println(path);


        form.setSeq(null);
        form.setUrl("https://mimgnews.pstatic.net/image/origin/138/2024/12/09/2187658.jpg");
        String path2 = service.getThumbPath(0L,"https://mimgnews.pstatic.net/image/origin/138/2024/12/09/2187658.jpg",100,100);
        path2 = service.create(form);
        System.out.println(path2);
    }
}
