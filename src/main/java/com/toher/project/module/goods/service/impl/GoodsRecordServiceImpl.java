package com.toher.project.module.goods.service.impl;

import com.toher.project.module.goods.entity.GoodsRecord;
import com.toher.project.module.goods.service.GoodsRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class GoodsRecordServiceImpl implements GoodsRecordService {
    @Override
    public GoodsRecord findObjectByPrimaryKey(Integer o) {
        return null;
    }

    @Override
    public GoodsRecord findObjectByEntity(GoodsRecord goodsRecord) {
        return null;
    }

    @Override
    public List<GoodsRecord> getObjects(Map<String, Object> map) {
        return null;
    }

    @Override
    public List<GoodsRecord> getObjectsByEntity(GoodsRecord goodsRecord) {
        return null;
    }

    @Override
    public int saveObject(GoodsRecord goodsRecord) {
        return 0;
    }

    @Override
    public int editObject(GoodsRecord goodsRecord) {
        return 0;
    }

    @Override
    public int saveObjectSelective(GoodsRecord goodsRecord) {
        return 0;
    }

    @Override
    public int editObjectSelective(GoodsRecord goodsRecord) {
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
