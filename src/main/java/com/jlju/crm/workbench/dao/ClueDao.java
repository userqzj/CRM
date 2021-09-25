package com.jlju.crm.workbench.dao;


import com.jlju.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {


    int save(Clue clue);

    Clue detail(String id);

    Clue getById(String clueId);

    int delete(String clueId);

    Integer getTotalByCondition(Map<String, Object> map);

    List<Clue> pageList(Map<String, Object> map);
}
