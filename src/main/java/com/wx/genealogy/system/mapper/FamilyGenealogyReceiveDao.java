package com.wx.genealogy.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wx.genealogy.system.entity.FamilyGenealogy;
import com.wx.genealogy.system.entity.FamilyGenealogyReceive;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 认领族谱图申请 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2023-03-03
 */
@Mapper
public interface FamilyGenealogyReceiveDao extends BaseMapper<FamilyGenealogyReceive> {
    /**
     * 查询需要审核的认领申请
     *
     * @param receive
     * @return
     */
    List<FamilyGenealogy> selectAuditApply(FamilyGenealogyReceive receive);

    /**
     * 查询需要审核的认领申请
     *
     * @param receive
     * @return
     */
    List<FamilyGenealogy> selectApply(FamilyGenealogyReceive receive);
}
