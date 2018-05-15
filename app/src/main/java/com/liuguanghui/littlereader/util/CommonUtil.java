package com.liuguanghui.littlereader.util;

import com.liuguanghui.littlereader.pojo.NovelVO;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by liuguanghui on 2018/5/10.
 */

public class CommonUtil {

    /**
     * 先以置顶（阅读时间）排
     * 未置顶的以阅读时间从大到小排
     */
    public static void  sortDesc(List<NovelVO> list){
        Collections.sort(list, new Comparator<NovelVO>() {
            @Override
            public int compare(NovelVO o1, NovelVO o2) {
                if(Integer.valueOf(o2.getIsTop()).compareTo(Integer.valueOf(o1.getIsTop())) == 0){
                    return  Long.valueOf(o2.getReadDate()).compareTo(Long.valueOf(o1.getReadDate()));
                }else{
                    return Integer.valueOf(o2.getIsTop()).compareTo(Integer.valueOf(o1.getIsTop()));
                }

            }
        });
    }

}
