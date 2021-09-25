package com.jlju.crm.workbench.service;

import com.jlju.crm.workbench.domain.Clue;
import com.jlju.crm.workbench.domain.Tran;

import java.util.Map;

public interface ClueService {
    boolean save(Clue clue);

    Clue detail(String id);

    boolean unbund(String id);


    boolean bund(String cid, String[] aids);


    boolean convert(String clueId, Tran t, String createBy);

    Map<String, Object> pageList(Map<String, Object> map);
}
