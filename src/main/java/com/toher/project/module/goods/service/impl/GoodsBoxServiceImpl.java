package com.toher.project.module.goods.service.impl;

import com.toher.project.module.goods.entity.GoodsBox;
import com.toher.project.module.goods.service.GoodsBoxService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class GoodsBoxServiceImpl implements GoodsBoxService {
    @Override
    public GoodsBox findObjectByPrimaryKey(Integer o) {
        return null;
    }

    @Override
    public GoodsBox findObjectByEntity(GoodsBox goodsBox) {
        return null;
    }

    @Override
    public List<GoodsBox> getObjects(Map<String, Object> map) {
        return null;
    }

    @Override
    public List<GoodsBox> getObjectsByEntity(GoodsBox goodsBox) {
        return null;
    }

    @Override
    public int saveObject(GoodsBox goodsBox) {
        return 0;
    }

    @Override
    public int editObject(GoodsBox goodsBox) {
        return 0;
    }

    @Override
    public int saveObjectSelective(GoodsBox goodsBox) {
        return 0;
    }

    @Override
    public int editObjectSelective(GoodsBox goodsBox) {
        return 0;
    }

    @Override
    public int deleteObjectByPrimaryKey(Integer o) {
        return 0;
    }

    @Override
    public int deleteBatchObject(List<Integer> data) {
        return 0;
    }
}
