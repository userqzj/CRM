package com.jlju.crm.settings.dao;

import com.jlju.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getListByCode(String code);
}
