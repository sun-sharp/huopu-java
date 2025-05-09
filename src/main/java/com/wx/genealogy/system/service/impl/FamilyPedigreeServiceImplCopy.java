package com.wx.genealogy.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ExcelUtils.ExcelUtils;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.FamilyPedigree;
import com.wx.genealogy.system.mapper.FamilyPedigreeDao;
import com.wx.genealogy.system.service.FamilyPedigreeService;
import com.wx.genealogy.system.vo.req.FamilyPedigreeAndFatherVo;
import com.wx.genealogy.system.vo.req.FamilyPedigreeExcelVo;
import com.wx.genealogy.system.vo.req.FamilyPedigreeRecursionVo;
import com.wx.genealogy.system.vo.req.FamilyUserAndPedigreeVo;
import net.sourceforge.pinyin4j.PinyinHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.function.Function;

//@Service
public class FamilyPedigreeServiceImplCopy implements FamilyPedigreeService {

    @Autowired
    private FamilyPedigreeDao familyPedigreeMapper;


    @Override
    public JsonResult getGeneration(Long id){


        return ResponseUtil.ok("获取成功", familyPedigreeMapper.selectGenerationCountsById(id));
    }


    @Override
    public JsonResult importFamilyPedigreeByUser(MultipartFile file, String jsonString) throws Exception {

        FamilyUserAndPedigreeVo familyUserAndPedigreeVo = new FamilyUserAndPedigreeVo();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            familyUserAndPedigreeVo = objectMapper.readValue(jsonString, FamilyUserAndPedigreeVo.class);
            System.out.println("User ID: " + familyUserAndPedigreeVo.getUserId());
            System.out.println("User Name: " + familyUserAndPedigreeVo.getName());
            System.out.println("用户信息："+ familyUserAndPedigreeVo.toString());

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("导入失败");
        }

        List<FamilyPedigreeExcelVo> familyPedigrees = null;
        List<FamilyPedigreeAndFatherVo> idList = new ArrayList<>();
        try {
            familyPedigrees = ExcelUtils.readMultipartFile(file, FamilyPedigreeExcelVo.class);
            for (FamilyPedigreeExcelVo familyPedigreeExcelVo : familyPedigrees) {
                FamilyPedigree familyPedigree = new FamilyPedigree();
                //数据

                familyPedigree.setIsDirect(familyPedigreeExcelVo.getIsDirect());//直属亲属
                familyPedigree.setName(familyPedigreeExcelVo.getName());//姓名
                familyPedigree.setSex(familyPedigreeExcelVo.getSex());//性别
                familyPedigree.setBirthday(familyPedigreeExcelVo.getBirthday());//生日
                familyPedigree.setEducation(familyPedigreeExcelVo.getEducation());//学历
                familyPedigree.setBlood(familyPedigreeExcelVo.getBlood());//血型
                familyPedigree.setPhone(familyPedigreeExcelVo.getPhone());//电话
                familyPedigree.setInitial(getPinyinFirstLetter(familyPedigreeExcelVo.getName()));//首字母


                //修改信息
                familyPedigree.setCreateBy(familyUserAndPedigreeVo.getName());
                familyPedigree.setUpdateBy(familyUserAndPedigreeVo.getName());
                Date date = new Date();
                familyPedigree.setCreateTime(date);
                familyPedigree.setUpdateTime(date);
                System.out.println(familyPedigree.toString());
//                System.out.println("插入数据之前："+ familyPedigree);
                insertFamilyPedigree(familyPedigree);
//                System.out.println("*****");
//                System.out.println("插入数据以后："+ familyPedigree);
//                System.out.println("***************分割线*****************");

                if(!"".equals(familyPedigreeExcelVo.getFather()) ){
                    FamilyPedigreeAndFatherVo familyPedigreeAndFatherVo = new FamilyPedigreeAndFatherVo();
                    familyPedigreeAndFatherVo.setId(familyPedigree.getId());
                    familyPedigreeAndFatherVo.setFather(familyPedigreeExcelVo.getFather());
                    familyPedigreeAndFatherVo.setIsDirect(familyPedigreeExcelVo.getIsDirect());
                    idList.add(familyPedigreeAndFatherVo);//记录存储后的数据
                }



            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("导入失败");
        }
//        System.out.println("已经导入的数据");
//        System.out.println(idList);
//        System.out.println("**************************");
        // 计算父级
        for (FamilyPedigreeAndFatherVo familyPedigreeAndFatherVo : idList) {
//            System.out.println("***************查找父级:" + familyPedigreeAndFatherVo.getId() + "  *****************");
            Long id = familyPedigreeAndFatherVo.getId();
            FamilyPedigree selectFamilyPedigree = new FamilyPedigree();
            FamilyPedigree familyPedigree = new FamilyPedigree();
            selectFamilyPedigree.setName(familyPedigreeAndFatherVo.getFather());//父亲姓名
            List<FamilyPedigree> familyPedigreeList = selectFamilyPedigreeList(selectFamilyPedigree);
//            System.out.println("查找到的数据："+familyPedigreeList);
//            System.out.println("查找的数据+"+selectFamilyPedigree);
//            System.out.println("查找到的数据条数："+familyPedigreeList.size());
            if(familyPedigreeList.size()!= 0){
                familyPedigree.setPid(familyPedigreeList.get(0).getId());
            }else{
                throw new Exception("人员：“"+ familyPedigreeAndFatherVo.getFather()+"”不存在！");
            }
            familyPedigree.setId(id);

            updateFamilyPedigree(familyPedigree);
//            System.out.println("修改数据："+ familyPedigree);
        }



        // 计算代
        for (FamilyPedigreeAndFatherVo familyPedigreeAndFatherVo : idList) {
//            System.out.println("***************计算谱:"+ familyPedigreeAndFatherVo.getId()+"  *****************");
            Long pid = familyPedigreeAndFatherVo.getId();
            Boolean doWhile = true;
            Long num = 0l;
            ArrayList<String> arrayList  = new ArrayList<String>();
            do {
                FamilyPedigree selectFamilyPedigree = selectFamilyPedigreeById(pid);
                if(selectFamilyPedigree == null ){
                    doWhile = false;
                }else{
                    pid = selectFamilyPedigree.getPid();
                    num++;
                    arrayList.add(selectFamilyPedigree.getName());
                    System.out.println(num);
                }
//                System.out.println("***************  "+num+" *****************");
//                System.out.println("查询到的数据："+ selectFamilyPedigree);

            } while (doWhile);
            FamilyPedigree familyPedigree = new FamilyPedigree();
            familyPedigree.setId(familyPedigreeAndFatherVo.getId());
            familyPedigree.setIteration(num);
            familyPedigree.setSpectrum(arrayList.toString());
            updateFamilyPedigree(familyPedigree);
        }



        return ResponseUtil.ok("导入成功");
    }

    public static String getPinyinFirstLetter(String chineseText) {
        StringBuilder sb = new StringBuilder();
        char[] chars = chineseText.toCharArray();

        for (char c : chars) {
            if (Character.toString(c).matches("[\\u4E00-\\u9FA5]+")) {
                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c);

                if (pinyinArray != null && pinyinArray.length > 0) {
                    sb.append(pinyinArray[0].charAt(0)); // 获取拼音的首字母
                }
            } else {
                sb.append(c); // 非中文字符直接追加
            }
        }

        return sb.toString().toUpperCase(); // 返回大写首字母
    }
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public FamilyPedigree selectFamilyPedigreeById(Long id)
    {
        return familyPedigreeMapper.selectFamilyPedigreeById(id);
    }

    @Override
    public FamilyPedigreeRecursionVo getFamilyPedigreeRecursion(Long id) {
        return null;
    }

    @Override
    public List<FamilyPedigreeRecursionVo> treeFamilyPedigreeRecursionVo() {
        return null;
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param familyPedigree 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<FamilyPedigree> selectFamilyPedigreeList(FamilyPedigree familyPedigree)
    {
        return familyPedigreeMapper.selectFamilyPedigreeList(familyPedigree);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param familyPedigree 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertFamilyPedigree(FamilyPedigree familyPedigree)
    {

        if(familyPedigree.getPid()!=null){
            updateFather(familyPedigree.getPid());//修改上级数据

        }else {

        }

        return familyPedigreeMapper.insertFamilyPedigree(familyPedigree);
    }


    //根据pid修改子级数据
    public void updateSon(Long pid){

    }


    //根据pid修改上级数据
    public void updateFather(Long id){
        Long pid = id;
        Boolean doWhile = true;
        Long num = 0l;
        ArrayList<String> arrayList  = new ArrayList<String>();
        do {
            FamilyPedigree selectFamilyPedigree = selectFamilyPedigreeById(pid);
            if(selectFamilyPedigree == null ){
                doWhile = false;
            }else{
                pid = selectFamilyPedigree.getPid();
                num++;
                arrayList.add(selectFamilyPedigree.getName());
                System.out.println(num);
            }
//                System.out.println("***************  "+num+" *****************");
//                System.out.println("查询到的数据："+ selectFamilyPedigree);

        } while (doWhile);
        FamilyPedigree familyPedigree = new FamilyPedigree();
        familyPedigree.setId(pid);
        familyPedigree.setIteration(num);
        familyPedigree.setSpectrum(arrayList.toString());
        System.out.println(arrayList.toString());
        updateFamilyPedigree(familyPedigree);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param familyPedigree 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateFamilyPedigree(FamilyPedigree familyPedigree)
    {
        return familyPedigreeMapper.updateFamilyPedigree(familyPedigree);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteFamilyPedigreeByIds(Long[] ids)
    {
        return familyPedigreeMapper.deleteFamilyPedigreeByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteFamilyPedigreeById(Long id)
    {
        return familyPedigreeMapper.deleteFamilyPedigreeById(id);
    }

    @Override
    public boolean saveBatch(Collection<FamilyPedigreeService> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<FamilyPedigreeService> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean updateBatchById(Collection<FamilyPedigreeService> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdate(FamilyPedigreeService entity) {
        return false;
    }

    @Override
    public FamilyPedigreeService getOne(Wrapper<FamilyPedigreeService> queryWrapper, boolean throwEx) {
        return null;
    }

    @Override
    public Map<String, Object> getMap(Wrapper<FamilyPedigreeService> queryWrapper) {
        return null;
    }

    @Override
    public <V> V getObj(Wrapper<FamilyPedigreeService> queryWrapper, Function<? super Object, V> mapper) {
        return null;
    }

    @Override
    public BaseMapper<FamilyPedigreeService> getBaseMapper() {
        return null;
    }

    @Override
    public Class<FamilyPedigreeService> getEntityClass() {
        return null;
    }
}
