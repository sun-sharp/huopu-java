package com.wx.genealogy.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.exception.ServiceException;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FamilyPedigreeServiceImpl implements FamilyPedigreeService {

    @Autowired
    private FamilyPedigreeDao familyPedigreeMapper;



    public FamilyPedigreeRecursionVo getFamilyPedigreeRecursion(Long id){
        FamilyPedigreeRecursionVo familyPedigreeRecursionVo = new FamilyPedigreeRecursionVo();
//        String uuid = UUID.randomUUID().toString();
//        familyPedigreeRecursionVo.setId(uuid);

        FamilyPedigree familyPedigree = new FamilyPedigree();
        familyPedigree.setFamilyId(id);
        Long iteration = 0l;
        List<FamilyPedigree> familyPedigreeList;
        List<FamilyPedigreeRecursionVo> familyPedigreeRecursionVoList;

        do{
            iteration++;
            FamilyPedigreeRecursionVo data = new FamilyPedigreeRecursionVo();
            familyPedigree.setIteration(iteration);
            familyPedigreeRecursionVoList =  familyPedigreeMapper.selectFamilyPedigreeRecursionVoList(familyPedigree);

            if(familyPedigreeRecursionVoList.size() < 1){//这一代没数据则退出
                break;
            }else if(iteration>1){
//                familyPedigreeRecursionVo.getFamilyPedigreeList();
            }
            else{
//                familyPedigreeRecursionVo.setFamilyPedigreeList(familyPedigreeRecursionVoList);
            }
            break;

        }while (true);
        return familyPedigreeRecursionVo;
    }


    @Override
    public JsonResult getGeneration(Long id){

        return ResponseUtil.ok("获取成功", familyPedigreeMapper.selectGenerationCountsById(id));
    }


    @Override
    public JsonResult importFamilyPedigreeByUser(MultipartFile file, String jsonString) throws Exception {

        FamilyUserAndPedigreeVo familyUserAndPedigreeVo = new FamilyUserAndPedigreeVo();

        Long familyId = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            familyUserAndPedigreeVo = objectMapper.readValue(jsonString, FamilyUserAndPedigreeVo.class);
            familyId = familyUserAndPedigreeVo.getFamilyId();
            List<FamilyPedigreeExcelVo> familyPedigrees = null;
            familyPedigrees = ExcelUtils.readMultipartFile(file, FamilyPedigreeExcelVo.class);
            for (FamilyPedigreeExcelVo familyPedigreeExcelVo : familyPedigrees) {
                FamilyPedigree familyPedigree = new FamilyPedigree();
                familyPedigree.setFamilyId(familyId);
                familyPedigree.setIsDirect(familyPedigreeExcelVo.getIsDirect());//直属亲属
                familyPedigree.setName(familyPedigreeExcelVo.getName());//姓名
                familyPedigree.setSex(familyPedigreeExcelVo.getSex());//性别
                familyPedigree.setBirthday(familyPedigreeExcelVo.getBirthday());//生日
                familyPedigree.setEducation(familyPedigreeExcelVo.getEducation());//学历
                familyPedigree.setBlood(familyPedigreeExcelVo.getBlood());//血型
                familyPedigree.setPhone(familyPedigreeExcelVo.getPhone());//电话
                familyPedigree.setInitial(getFirstLetter(familyPedigreeExcelVo.getName()));//首字母
                familyPedigree.setIteration(familyPedigreeExcelVo.getIteration());//家族代数
                familyPedigree.setMarriage(familyPedigreeExcelVo.getMarriage());

                //修改信息
                familyPedigree.setCreateBy(familyUserAndPedigreeVo.getName());
                familyPedigree.setUpdateBy(familyUserAndPedigreeVo.getName());
                Date date = new Date();
                familyPedigree.setCreateTime(date);
                familyPedigree.setUpdateTime(date);
                insertFamilyPedigree(familyPedigree);
                System.out.println(familyPedigreeExcelVo);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("导入失败");
        }
        return ResponseUtil.ok("导入成功");

    }


    public JsonResult importFamilyPedigreeByUsers(MultipartFile file, String jsonString) throws Exception {

        FamilyUserAndPedigreeVo familyUserAndPedigreeVo = new FamilyUserAndPedigreeVo();

        Long familyId = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            familyUserAndPedigreeVo = objectMapper.readValue(jsonString, FamilyUserAndPedigreeVo.class);
//            System.out.println("User ID: " + familyUserAndPedigreeVo.getUserId());
//            System.out.println("User Name: " + familyUserAndPedigreeVo.getName());
//            System.out.println("用户信息："+ familyUserAndPedigreeVo.toString());
            familyId = familyUserAndPedigreeVo.getFamilyId();

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("导入失败");
        }


        int failureNum = 0;
        StringBuilder failureMsg = new StringBuilder();



        List<FamilyPedigreeExcelVo> familyPedigrees = null;
        List<FamilyPedigreeAndFatherVo> idList = new ArrayList<>();
        try {
            familyPedigrees = ExcelUtils.readMultipartFile(file, FamilyPedigreeExcelVo.class);
            for (FamilyPedigreeExcelVo familyPedigreeExcelVo : familyPedigrees) {
//                System.out.println("申请："+familyPedigreeExcelVo);
                FamilyPedigree familyPedigree = new FamilyPedigree();
                familyPedigree.setFamilyId(familyId);
                //数据
                familyPedigree.setIsDirect(familyPedigreeExcelVo.getIsDirect());//直属亲属
                familyPedigree.setName(familyPedigreeExcelVo.getName());//姓名
                familyPedigree.setSex(familyPedigreeExcelVo.getSex());//性别
                familyPedigree.setBirthday(familyPedigreeExcelVo.getBirthday());//生日
                familyPedigree.setEducation(familyPedigreeExcelVo.getEducation());//学历
                familyPedigree.setBlood(familyPedigreeExcelVo.getBlood());//血型
                familyPedigree.setPhone(familyPedigreeExcelVo.getPhone());//电话
                familyPedigree.setInitial(getFirstLetter(familyPedigreeExcelVo.getName()));//首字母
                familyPedigree.setIteration(familyPedigreeExcelVo.getIteration());//家族代数
                familyPedigree.setMarriage(familyPedigreeExcelVo.getMarriage());

                //修改信息
                familyPedigree.setCreateBy(familyUserAndPedigreeVo.getName());
                familyPedigree.setUpdateBy(familyUserAndPedigreeVo.getName());
                Date date = new Date();
                familyPedigree.setCreateTime(date);
                familyPedigree.setUpdateTime(date);
//                System.out.println(familyPedigree.toString());
                insertFamilyPedigree(familyPedigree);
                System.out.println(familyPedigreeExcelVo);

//            if(familyPedigreeExcelVo.getIteration() > 1 ){
//                    FamilyPedigreeAndFatherVo familyPedigreeAndFatherVo = new FamilyPedigreeAndFatherVo();
//                    familyPedigreeAndFatherVo.setId(familyPedigree.getId());
//                    familyPedigreeAndFatherVo.setFather(familyPedigreeExcelVo.getFather());
//                    familyPedigreeAndFatherVo.setIsDirect(familyPedigreeExcelVo.getIsDirect());
//                    familyPedigreeAndFatherVo.setIteration(familyPedigreeExcelVo.getIteration());
//                    familyPedigreeAndFatherVo.setName(familyPedigreeExcelVo.getName());
//                    familyPedigreeAndFatherVo.setMarriage(familyPedigreeExcelVo.getMarriage());
//                    System.out.println("妻子名字："+familyPedigreeExcelVo.getMateName());
//                    familyPedigreeAndFatherVo.setMateName(familyPedigreeExcelVo.getMateName());
//                    idList.add(familyPedigreeAndFatherVo);//记录存储后的数据
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("导入失败");
        }

//        System.out.println("已经导入的数据");
//        System.out.println(idList);
//        System.out.println("**************************");


//
//
//        FamilyPedigree familyMate = new FamilyPedigree();
//
//        // 计算父级
//        for (FamilyPedigreeAndFatherVo familyPedigreeAndFatherVo : idList) {
//
//
//            Long id = familyPedigreeAndFatherVo.getId();
//            FamilyPedigree selectFamilyPedigree = new FamilyPedigree();//上级查询
//            selectFamilyPedigree.setFamilyId(familyId);
//            selectFamilyPedigree.setName(familyPedigreeAndFatherVo.getFather());//父亲姓名
//            selectFamilyPedigree.setIteration(familyPedigreeAndFatherVo.getIteration()-1);//父亲代数
//            List<FamilyPedigree> familyPedigreeList = selectFamilyPedigreeList(selectFamilyPedigree);
////            System.out.println("查询第"+familyPedigreeAndFatherVo.getIteration()+"代的人员："+ familyPedigreeAndFatherVo.getFather()+"，一共"+familyPedigreeList.size()+"条数据");
//            FamilyPedigree familyPedigree = new FamilyPedigree();//上级修改
//            if(familyPedigreeList.size()!= 0){
//                familyPedigree.setPid(familyPedigreeList.get(0).getId());
//                familyPedigree.setId(id);
//                updateFamilyPedigree(familyPedigree);
//            }else{
//                familyPedigree.setPid(null);
//                failureNum++;
//                String msg = "<br/>" + failureNum + "、 " +  familyPedigreeAndFatherVo.getName() + " 导入失败：";
//                String text = "第"+familyPedigreeAndFatherVo.getIteration()+"代的人员的上级：“"+ familyPedigreeAndFatherVo.getFather()+"”不存在！";
//                failureMsg.append(msg + text);
////                return ResponseUtil.unkownException("第"+familyPedigreeAndFatherVo.getIteration()+"代的人员：“"+ familyPedigreeAndFatherVo.getFather()+"”不存在！");
//            }
//            //婚姻id
//            System.out.println("判断婚姻");
//            if(familyPedigreeAndFatherVo.getMarriage() == 1){
//                if(familyPedigreeAndFatherVo.getMateName() == null){
//                    failureNum++;
//                    String msg = "<br/>" + failureNum + "、 " +  familyPedigreeAndFatherVo.getName() + " 导入失败：";
//                    String text = "第"+familyPedigreeAndFatherVo.getIteration()+"代的人员的妻子：“"+ familyPedigreeAndFatherVo.getFather()+"”为空！";
//                    failureMsg.append(msg + text);
//                }
//                System.out.println("进入婚姻");
//                FamilyPedigree selectMarriage = new FamilyPedigree();//配偶查询
//                FamilyPedigree familyMarriage = new FamilyPedigree();//配偶修改
//                selectMarriage.setFamilyId(familyId);
//                selectMarriage.setIteration(familyPedigreeAndFatherVo.getIteration());
//                System.out.println(familyPedigreeAndFatherVo);
//                if(familyPedigreeAndFatherVo.getIsDirect()==1){
//                    List<FamilyPedigree> MarriageList = selectFamilyPedigreeList(selectMarriage);
//                    if(MarriageList.size()!=0){
//                        familyMarriage.setMateId(MarriageList.get(0).getId());
//                        familyMarriage.setId(familyPedigreeAndFatherVo.getId());
//                        updateFamilyPedigree(familyMarriage);
//                    }else{
//                        failureNum++;
//                        String msg = "<br/>" + failureNum + "、 " +  familyPedigreeAndFatherVo.getName() + " 导入失败：";
//                        String text = "第"+familyPedigreeAndFatherVo.getIteration()+"代的人员的配偶：“"+ familyPedigreeAndFatherVo.getFather()+"”不存在！";
//                        failureMsg.append(msg + text);
//                        throw new ServiceException("有问题捏"+familyMarriage);
//                    }
//                }
//            }

//        }

        if(failureNum > 0){
            throw new ServiceException(failureMsg+"");
        }

        return ResponseUtil.ok("导入成功");
    }

    public static String getFirstLetter(String chineseText) {
        if (chineseText != null && !chineseText.isEmpty()) {
            char firstChar = chineseText.charAt(0);
            if (Character.toString(firstChar).matches("[\\u4E00-\\u9FA5]+")) {
                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(firstChar);

                if (pinyinArray != null && pinyinArray.length > 0) {
                    return String.valueOf(pinyinArray[0].charAt(0)).toUpperCase();
                }
            }
        }
        return ""; // 如果输入为空或不是中文字符，则返回空字符串
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
        if(selectFamilyPedigreeList(familyPedigree).size()>1){
            throw new ServiceException("该人员已存在");
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

    @Override
    public List<FamilyPedigreeRecursionVo> treeFamilyPedigreeRecursionVo() {
        FamilyPedigree familyPedigree = new FamilyPedigree();
        Long id = 1l;
        familyPedigree.setFamilyId(id);
//        familyPedigree.setIteration(id);
        List<FamilyPedigreeRecursionVo> treeBeans = familyPedigreeMapper.selectFamilyPedigreeRecursionVoList(familyPedigree);

        System.out.println();

        //获取父节点
        List<FamilyPedigreeRecursionVo> collect = treeBeans.stream().filter(t -> t.getPid() == 0).map(
                m -> {
                    m.setChildren(getChildren(m, treeBeans));
                    return m;
                }
        ).collect(Collectors.toList());
//        System.out.println(JSON.toJSONString(collect));
        return collect;


    }

    /**
     * 递归查询子节点
     * @param root  根节点
     * @param all   所有节点
     * @return 根节点信息
     */
    public static List<FamilyPedigreeRecursionVo> getChildren(FamilyPedigreeRecursionVo root, List<FamilyPedigreeRecursionVo> all) {
        List<FamilyPedigreeRecursionVo> children = all.stream().filter(t -> {
            return Objects.equals(t.getPid(), root.getId());
        }).map(
                m -> {
                    m.setChildren(getChildren(m, all));
                    return m;
                }
        ).collect(Collectors.toList());
        return children;
    }
}
