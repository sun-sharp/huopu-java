package com.wx.genealogy;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


import com.wx.genealogy.common.util.JavaMailUntil;
import com.wx.genealogy.system.entity.*;
import com.wx.genealogy.system.mapper.*;
import com.wx.genealogy.system.service.FamilyGenealogyService;
import com.wx.genealogy.system.service.FamilyManageLogService;
import com.wx.genealogy.system.service.FamilyMessageService;

import com.wx.genealogy.system.service.FamilyUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
class GenealogyApplicationTests {



    @Autowired
    private FamilyGenealogyDao familyGenealogyDao;


    @Autowired
    private FamilyMessageDao familyMessageDao;


    @Autowired
    private FamilyUserDao familyUserDao;


@Autowired
private FamilyManageLogService familyManageLogService;



    @Autowired
    private FamilyMessageService familyMessageService;

    @Autowired
    private FamilyGenealogyService familyGenealogyService;

    @Autowired
    private FamilyUserService familyUserService;




//    @Autowired
//    private

    @Test
    void contextLoadsA1() {
        Map<Integer, Integer> parentIdMap = new HashMap<>();
        parentIdMap.put(1, null);
        parentIdMap.put(2, null);
        parentIdMap.put(3, null);
        parentIdMap.put(4, 2);
        parentIdMap.put(5, 6);
        parentIdMap.put(6, 4);
        parentIdMap.put(7, 4);
        System.out.println(        this.findCommonParent(parentIdMap,5,6)
);
    }


    public static int findCommonParent(Map<Integer, Integer> parentIdMap, int uid1, int uid2) {
        int parent1 = uid1;
        int parent2 = uid2;

        // 逐级查找直到找到共同父级或到达根节点
        while (parent1 != parent2) {
            Integer parent1Value = parentIdMap.get(parent1);
            Integer parent2Value = parentIdMap.get(parent2);

            if (parent1Value == null && parent2Value == null) {
                // 如果两个用户都没有父级了，说明没有共同父级
                return 0;
            }

            if (parent1Value != null) {
                parent1 = parent1Value;
            }

            if (parent2Value != null) {
                parent2 = parent2Value;
            }
        }

        return parent1; // 返回共同父级
    }

    @Test
    void e(){
//        familyUserService.updateFamilyUp(131,2,"我就");
//        List<FamilyGenealogy> list = familyGenealogyDao.selectNameGenerationListOne();
//        for (FamilyGenealogy familyGenealogy: list
//             ) {
//            QueryWrapper<FamilyGenealogy> familyGenealogySelect = new QueryWrapper<FamilyGenealogy>();
//            familyGenealogySelect.eq("family_id",familyGenealogy.getFamilyId());
//            List<FamilyGenealogy> familyList = familyGenealogyDao.selectList(familyGenealogySelect);
//            int i = 1;
//            for (FamilyGenealogy fUid:familyList
//                 ) {
//                FamilyGenealogy fg = new FamilyGenealogy();
//                fg.setId(fUid.getId());
//                fg.setUid(i++);
//                familyGenealogyDao.updatePidFamilyId(fg);
//            }
//        }
    }

    @Test
    void contextLoadsA() {
//        for (int i = 0;i<4;i++){
//            FamilyGenealogy familyGenealogy = new FamilyGenealogy();
//            familyGenealogy.setFamilyId(128);
//            familyGenealogy.setSex(1);
//            familyGenealogy.setRelation(1);
//            familyGenealogy.setIdentity(1);
//            familyGenealogy.setGeneration(5);
//            familyGenealogy.setGenealogyName("王五"+i);
//            familyGenealogyDao.insert(familyGenealogy);
//        }
    }


    @Test
    void contextLoadsB() {
//        FamilyMessage familyMessage = new FamilyMessage();
//        familyMessage.setFamilyId(126);
//        familyMessage.setUserId(1058);
//        familyMessage.setFamilyshenMessage2(200);
//        System.out.println(familyMessage);
//        familyMessageDao.updateFamilyMessage(familyMessage);
    }

    @Test
    void contextLoadsBc()  {
//        System.out.println( familyGenealogyDao.selectUidMax(126));
//
//        System.out.println("*****");
//
//        FamilyGenealogy familyGenealogy = new  FamilyGenealogy();
//        familyGenealogy.setGeneration(5);
//        familyGenealogy.setFamilyId(126);
//        familyGenealogy.setGenealogyName("面包5");
//        System.out.println(familyGenealogyDao.selectNameGenerationList(familyGenealogy));
//        QueryWrapper<FamilyGenealogy> familyGenealogySelect = new QueryWrapper<FamilyGenealogy>();
//        familyGenealogySelect.eq("family_id", 140);
//        List<FamilyGenealogy> familyGenealogies = familyGenealogyDao.selectList(familyGenealogySelect);//这个家族成员全部数据
//        System.out.println(familyGenealogies);
    }





}
