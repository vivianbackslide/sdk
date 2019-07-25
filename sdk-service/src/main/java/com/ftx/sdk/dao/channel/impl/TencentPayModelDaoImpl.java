package com.ftx.sdk.dao.channel.impl;

import com.ftx.sdk.dao.channel.TencentPayModelDao;
import com.ftx.sdk.model.TencentPayModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class TencentPayModelDaoImpl implements TencentPayModelDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Override
    public List<TencentPayModel> getAll() {
        String sql = "select open_id openId, open_key openKey, pf, pf_key pfKey, zone_id zoneId, billno, is_qq isQQ, amt from t_tencent_pay_model";
        Object[] objects = {};
        return jdbcTemplate.query(sql, objects, new BeanPropertyRowMapper<>(TencentPayModel.class));
    }

    @Override
    public void insert(TencentPayModel model) {
        String sql = "INSERT INTO t_tencent_pay_model(`open_id`, `open_key`, `pf`, `pf_key`, `zone_id`, `billno`, `is_qq`, `amt`) select ?, ?, ?, ?, ?, ?, ?, ? from dual where not exists (select  1 from t_tencent_pay_model where billno = ?)";
        Object[] objects = {model.getOpenId(), model.getOpenKey(), model.getPf(), model.getPfKey(), model.getZoneId(), model.getBillno(), model.getIsQQ(), model.getAmt(),model.getBillno()};
        jdbcTemplate.update(sql, objects);
    }

    @Override
    public void batchDelete(List<TencentPayModel> list) {
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(list.toArray());
        namedParameterJdbcTemplate.batchUpdate("delete from t_tencent_pay_model where billno=:billno", batch);
    }


}
