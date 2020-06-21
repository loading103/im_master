package com.android.nettylibrary.greendao.greenutils;


import android.text.TextUtils;
import android.util.Log;

import com.android.nettylibrary.IMSNettyManager;
import com.android.nettylibrary.greendao.GreenDaoManager;
import com.android.nettylibrary.greendao.entity.IMChatBgBean;
import com.android.nettylibrary.greendao.entity.IMConversationBean;
import com.android.nettylibrary.greendao.entity.IMConversationDetailBean;
import com.android.nettylibrary.greendao.entity.IMGroupBean;
import com.android.nettylibrary.greendao.entity.IMGroupMemberBean;
import com.android.nettylibrary.greendao.entity.IMPersonBean;
import com.android.nettylibrary.greendao.entity.IMSystemBean;
import com.android.nettylibrary.greendao.gen.IMChatBgBeanDao;
import com.android.nettylibrary.greendao.gen.IMConversationBeanDao;
import com.android.nettylibrary.greendao.gen.IMConversationDetailBeanDao;
import com.android.nettylibrary.greendao.gen.IMGroupBeanDao;
import com.android.nettylibrary.greendao.gen.IMGroupMemberBeanDao;
import com.android.nettylibrary.greendao.gen.IMPersonBeanDao;
import com.android.nettylibrary.greendao.gen.IMSystemBeanDao;
import com.android.nettylibrary.utils.IMLogUtil;

import java.util.List;

public class DaoUtils {
    private static final String TAG = "GreenDaoManager";

    private static DaoUtils instance;

    private final IMPersonBeanDao messageBeanDao;


    private final IMGroupBeanDao mGroupBeanDao;


    private final IMGroupMemberBeanDao mGroupMemberBeanDao;

    private final IMConversationBeanDao mConversationBeanDao;

    private final IMConversationDetailBeanDao mConversationDetailBeanDao;

    private final IMSystemBeanDao mImSystemBeanDao;

    private final IMChatBgBeanDao mImChatBeanDao;
    public static DaoUtils getInstance() {
        if (instance == null) {
            synchronized (GreenDaoManager.class) {
                if (instance == null) {
                    instance = new DaoUtils();
                }
            }
        }
        return instance;
    }

    private DaoUtils() {
        messageBeanDao =  GreenDaoManager.getInstance().getSession().getIMPersonBeanDao();
        mGroupBeanDao =  GreenDaoManager.getInstance().getSession().getIMGroupBeanDao();
        mGroupMemberBeanDao=  GreenDaoManager.getInstance().getSession().getIMGroupMemberBeanDao();
        mConversationBeanDao=  GreenDaoManager.getInstance().getSession().getIMConversationBeanDao();
        mConversationDetailBeanDao=  GreenDaoManager.getInstance().getSession().getIMConversationDetailBeanDao();
        mImSystemBeanDao=  GreenDaoManager.getInstance().getSession().getIMSystemBeanDao();
        mImChatBeanDao=  GreenDaoManager.getInstance().getSession().getIMChatBgBeanDao();
    }
    //*************************************聊天背景的增删改查****************************************
    public void insertChatData(IMChatBgBean bean) {
        if(TextUtils.isEmpty(bean.getUrl())){
            return;
        }
        IMChatBgBean bean1 = queryChatBgBean(bean.getUrl());
        if(bean1==null){
            mImChatBeanDao.insert(bean);
        }
    }
    /**
     * 更新聊天背景
     */
    public void updateChatBgData(IMChatBgBean bean) {
        mImChatBeanDao.update(bean);
    }
    /**
     * 根据id查询聊天背景
     */
    public IMChatBgBean queryChatBgBean(String url) {
        try {
            IMChatBgBean  bean = mImChatBeanDao.queryBuilder()
                    .where(IMChatBgBeanDao.Properties.Url.eq(url))
                    .where(IMChatBgBeanDao.Properties.Ismine.eq(IMSNettyManager.getMyUseId()))
                    .build().unique();
            return bean;
        }catch (Exception e){
            return null;
        }
    }
    /**
     * 根据id查询聊天背景
     */
    public List<IMChatBgBean> queryChatBgBean() {
        try {
            List<IMChatBgBean>  bean = mImChatBeanDao.queryBuilder()
                    .where(IMChatBgBeanDao.Properties.Ismine.eq(IMSNettyManager.getMyUseId()))
                    .build().list();
            return bean;
        }catch (Exception e){
            return null;
        }
    }
    //*************************************SystemBean的增删改查****************************************
    public void insertSystemData(IMSystemBean bean) {
        if(TextUtils.isEmpty(bean.getFingerprint())){
            return;
        }
        IMSystemBean bean1 = querySystemBean(bean.getFingerprint());
        if(bean1==null){
            mImSystemBeanDao.insert(bean);
        }
    }
    /**
     * 根据id查询查询用户列表
     */
    public IMSystemBean querySystemBean(String Fingerprint) {
        try {
            IMSystemBean  bean = mImSystemBeanDao.queryBuilder()
                    .where(IMSystemBeanDao.Properties.Fingerprint.eq(Fingerprint))
                    .build().unique();
            return bean;
        }catch (Exception e){
            return null;
        }
    }

    //*************************************MessageBean的增删改查****************************************

    /**
     * 插入parent数据
     *
     * @param bean
     * @return
     */
    public void insertMessageData(IMPersonBean bean) {
        if(TextUtils.isEmpty(bean.getMemberId())){
            return;
        }
        IMPersonBean bean1 = queryMessageBean(bean.getMemberId());
        if(bean1==null){
            messageBeanDao.insert(bean);
        }
    }

    public void deleteMessageData() {
        messageBeanDao.deleteAll();
    }
    /**
     * 更新parent数据
     *
     * @param bean
     */
    public void updateMessageData(IMPersonBean bean) {
        messageBeanDao.update(bean);
    }
    /**
     * 查询所有的user数据
     *根据时间排序）降序
     * @return
     */
    public List<IMPersonBean> queryAllMessageData() {
        //两种方式获取是一样的，查看源码便知
        try{
            List<IMPersonBean> list = messageBeanDao.queryBuilder().orderDesc(IMPersonBeanDao.Properties.LastMessageTime).list();
            return list;
        }catch (Exception e){
            return null;
        }
    }
    /**
     * 根据id查询查询用户列表
     */
    public IMPersonBean queryMessageBean(String userid) {
        try{
            IMPersonBean bean = messageBeanDao.queryBuilder().where(IMPersonBeanDao.Properties.MemberId.eq(userid)).build().unique();
            return bean;
        }catch (Exception e){
            return null;
        }
    }

    //*************************************GroupBean的增删改查****************************************

    /**
     * 插入parent数据
     *
     * @param bean
     * @return
     */
    public void insertGroupData(IMGroupBean bean) {
        if(TextUtils.isEmpty(bean.getGroupId())){
            return;
        }
        IMGroupBean groupBean = queryGroupBean(bean.getGroupId());
        if(groupBean==null){
            mGroupBeanDao.insert(bean);
        }
    }

    /**
     * 删除parent数据
     *
     */
    public void deleteGroupData(IMGroupBean bean) {
        mGroupBeanDao.delete(bean);
    }
    /**
     * 删除parent数据
     *
     */
    public void deleteGroupData(String groupId) {
        IMGroupBean bean = mGroupBeanDao.queryBuilder().where(IMGroupBeanDao.Properties.GroupId.eq(groupId)).build().unique();
        if(bean==null){
            return;
        }
        deleteGroupData(bean);
    }

    public void deleteGroupAllData() {
        mGroupBeanDao.deleteAll();
    }
    /**
     * 更新parent数据
     *
     * @param bean
     */
    public void updateGroupData(IMGroupBean bean) {
        mGroupBeanDao.update(bean);
    }
    /**
     * 查询所有的user数据
     *根据时间排序）降序
     * @return
     */
    public List<IMGroupBean> queryAllGroupData() {
        //两种方式获取是一样的，查看源码便知
        try {
            List<IMGroupBean> list = mGroupBeanDao.queryBuilder().orderDesc(IMGroupBeanDao.Properties.LastMessageTime).list();
            return list;
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 根据id查询查询用户列表
     */
    public IMGroupBean queryGroupBean(String groupId) {
        try {
            IMGroupBean bean = mGroupBeanDao.queryBuilder().where(IMGroupBeanDao.Properties.GroupId.eq(groupId)).build().unique();
            return bean;
        } catch (Exception e) {
            return null;
        }
    }

    //*************************************GroupMemberBean的增删改查****************************************

    /**
     * 插入parent数据
     *
     * @param bean
     * @return
     */
    public void insertGroupMemberData(String groupId,IMGroupMemberBean bean) {
        if(TextUtils.isEmpty(bean.getMemberId())){
            return;
        }
        IMGroupMemberBean bean1 = queryGroupMemberBean(groupId, bean.getMemberId());
        if(bean1==null) {
            mGroupMemberBeanDao.insert(bean);
        }
    }

    /**
     * 删除parent数据
     *
     * @param
     */
    public void deleteGroupAllMemberData(String groupId) {
        List<IMGroupMemberBean> beans = queryGroupAllMemberBean(groupId);
        if(beans!=null && beans.size()>0){
            for (int i = 0; i < beans.size(); i++) {
                mGroupMemberBeanDao.delete(beans.get(i));
            }
        }
    }
    public void deleteGroupMemberData(String groupId,String memberId) {
        IMGroupMemberBean bean = mGroupMemberBeanDao.queryBuilder().where(IMGroupMemberBeanDao.Properties.GroupId.eq(groupId))
                .where(IMGroupMemberBeanDao.Properties.MemberId.eq(memberId)).build().unique();
        if(bean==null){
            return;
        }
        mGroupMemberBeanDao.delete(bean);
    }

    public void deleteGroupMemberData(String groupId) {
        List<IMGroupMemberBean> list = mGroupMemberBeanDao.queryBuilder().where(IMGroupMemberBeanDao.Properties.GroupId.eq(groupId))
                .build().list();
        if(list==null || list.size()==0){
            return;
        }
        for (int i = 0; i <list.size() ; i++) {
            mGroupMemberBeanDao.delete(list.get(i));
        }
    }
    /**
     * 更新parent数据
     */
    public void updateGroupMemberData(IMGroupMemberBean bean) {
        mGroupMemberBeanDao.update(bean);
    }
    /**
     * 根据群id查询查询用户列表（每个群）
     */
    public List <IMGroupMemberBean> queryGroupAllMemberBean(String groupId) {
        try{
            List <IMGroupMemberBean> bean = mGroupMemberBeanDao.queryBuilder().where(IMGroupMemberBeanDao.Properties.GroupId.eq(groupId)).build().list();
            return bean;
        }catch (Exception e){
            return null;
        }
    }

    public List <IMGroupMemberBean> queryGroupAllMemberBean(String groupId,int number) {
        try{
            List <IMGroupMemberBean> bean = mGroupMemberBeanDao.queryBuilder().
                    where(IMGroupMemberBeanDao.Properties.GroupId.eq(groupId))
                    .limit(number).build().list();
            return bean;
        }catch (Exception e){
            return null;
        }
    }
    /**
     * 根据群id查询查单个用户
     */
    public IMGroupMemberBean queryGroupMemberBean(String groupId,String memberId) {
        try{
            IMGroupMemberBean  bean = mGroupMemberBeanDao.queryBuilder().where(IMGroupMemberBeanDao.Properties.GroupId.eq(groupId))
                    .where(IMGroupMemberBeanDao.Properties.MemberId.eq(memberId)).build().unique();
            return bean;
        }catch (Exception e){
            return null;
        }
    }

    //*************************************IMConversationBean的增删改查****************************************

    /**
     * 插入parent数据
     *
     * @param bean
     * @return
     */
    public void insertConversationData(IMConversationBean bean) {
        String myUseId = IMSNettyManager.getMyUseId();
        bean.setCurrentUid(myUseId);
        IMConversationBean bean1 = queryConversationBean(bean.getConversationId());
        if(bean1==null){
            IMLogUtil.d("MyOwnTag:", "IMConversationFragment创建群聊刷新界面 插入数据库----" +bean.getConversationId());
            mConversationBeanDao.insert(bean);
        }
    }
    /**
     * 删除parent数据
     *
     * @param id
     */
    public void deleteConversationData(Long id) {
        mConversationBeanDao.deleteByKey(id);
    }
    public void deleteConversationData(String ConversationID) {
        IMConversationBean bean = queryConversationBean(ConversationID);
        if(bean==null){
            return;
        }
        mConversationBeanDao.delete(bean);
    }
    public void deleteConversationData(IMConversationBean bean) {
        if(bean==null){
            return;
        }
        mConversationBeanDao.delete(bean);
    }
    public void deleteConversationAllData() {
        mConversationBeanDao.deleteAll();
    }
    /**
     * 更新parent数据
     *
     * @param bean
     */
    public void updateConversationData(IMConversationBean bean) {
        IMConversationBean bean1 = queryConversationBean(bean.getConversationId());
        if(bean1==null){
            mConversationBeanDao.insert(bean);
        }else {
            mConversationBeanDao.update(bean);
        }
    }
    /**
     * 查询所有的user数据
     *根据时间排序）降序
     * @return
     */
    public List<IMConversationBean> queryAllConversationData() {
        try {
            List<IMConversationBean> list = mConversationBeanDao.queryBuilder().where(IMConversationBeanDao.Properties.CurrentUid.eq(IMSNettyManager.getMyUseId())).orderDesc(IMConversationBeanDao.Properties.LastMessageTime).list();
            return list;
        }catch (Exception e){
            return null;
        }
    }
    public List<IMConversationBean> queryAllConversationData(int size) {
        try {
            List<IMConversationBean> list = mConversationBeanDao.queryBuilder()
                    .where(IMConversationBeanDao.Properties.CurrentUid.eq(IMSNettyManager.getMyUseId()))
                    .limit(size).orderDesc(IMConversationBeanDao.Properties.LastMessageTime).list();
            return list;
        }catch (Exception e){
            return null;
        }
    }
    /**
     * 根据id查询查询用户列表
     */
    public IMConversationBean queryConversationBean(String conversationId) {
        try {
            IMConversationBean bean = mConversationBeanDao.queryBuilder()
                    .where(IMConversationBeanDao.Properties.CurrentUid.eq(IMSNettyManager.getMyUseId()))
                    .where(IMConversationBeanDao.Properties.ConversationId.eq(conversationId)).build().unique();
            return bean;
        }catch (Exception e){
            return null;
        }
    }
    /**
     * 根据指纹查询查询用户列表
     */
    public IMConversationBean queryConversationBeanByFinger(String lastmessageId) {
        try {
            IMConversationBean bean = mConversationBeanDao.queryBuilder()
                    .where(IMConversationBeanDao.Properties.CurrentUid.eq(IMSNettyManager.getMyUseId()))
                    .where(IMConversationBeanDao.Properties.LastMessageId.eq(lastmessageId)).build().unique();
            return bean;
        }catch (Exception e){
            return null;
        }
    }

    //*************************************ConversationDetailDao的增删改查****************************************

    /**
     * 插入children数据
     *
     */
    public void insertConversationDetailData(IMConversationDetailBean bean) {
        IMConversationDetailBean data = queryConversationDetailDataAccordFieldFiger(bean.getFingerprint());
        if(data==null){
            bean.setCurrentUid(IMSNettyManager.getMyUseId());
            mConversationDetailBeanDao.insert(bean);
        }

    }

    /**
     * 删除children数据
     *
     * @param id
     */
    public void deleteConversationDetailData(Long id) {
        mConversationDetailBeanDao.deleteByKey(id);
    }
    public void deleteConversationDetailData(IMConversationDetailBean bean) {
        mConversationDetailBeanDao.delete(bean);
    }
    /**
     * 删除children数据
     */
    public void deleteAllConversationDetailData() {
        mConversationDetailBeanDao.deleteAll();
    }
    /**
     * 更新children数据
     *
     * @param bean
     */
    public void updateConversationDetailData(IMConversationDetailBean bean) {
        IMConversationDetailBean bean1 = queryConversationDetailDataAccordFieldFiger(bean.getFingerprint());
        if(bean1==null){
            mConversationDetailBeanDao.insert(bean1);
        }else {
            mConversationDetailBeanDao.update(bean1);
        }
    }

    /**
     * 查询某个ConversationId下对应的所有message
     * @return
     */
    public List<IMConversationDetailBean> queryConversationDetailDataAccordField(String userId) {
        try {
            List<IMConversationDetailBean> list = mConversationDetailBeanDao.queryBuilder()
                    .where(IMConversationDetailBeanDao.Properties.ConversationId.eq(userId))
                    .where(IMConversationDetailBeanDao.Properties.CurrentUid.eq(IMSNettyManager.getMyUseId()))
                    .orderDesc(IMConversationDetailBeanDao.Properties.Timestamp)
                    .list();
            return list;
        } catch (Exception e) {
            return null;
        }
    }

    public List<IMConversationDetailBean> queryConversationDetailDataAccordField(String ConversationId,int offset,int pageSize) {
        try {
            List<IMConversationDetailBean> list = mConversationDetailBeanDao.queryBuilder()
                    .where(IMConversationDetailBeanDao.Properties.ConversationId.eq(ConversationId))
                    .where(IMConversationDetailBeanDao.Properties.CurrentUid.eq(IMSNettyManager.getMyUseId()))
                    .orderDesc(IMConversationDetailBeanDao.Properties.Timestamp)
                    .offset(offset).limit(pageSize)
                    .list();
            return list;
        } catch (Exception e) {
            return null;
        }
    }
    public IMConversationDetailBean queryConversationDetailDataAccordFieldFiger(String messageID) {
        try {
            IMConversationDetailBean list = mConversationDetailBeanDao.queryBuilder()
                    .where(IMConversationDetailBeanDao.Properties.Fingerprint.eq(messageID))
                    .where(IMConversationDetailBeanDao.Properties.CurrentUid.eq(IMSNettyManager.getMyUseId()))
                    .unique();
            return list;
        }catch (Exception e){
            return null;
        }
    }
    //升序
    public List<IMConversationDetailBean> queryConversationDetailorderAscrdField(String ConversationId) {
        try {
            List<IMConversationDetailBean> list = mConversationDetailBeanDao.queryBuilder()
                    .where(IMConversationDetailBeanDao.Properties.ConversationId.eq(ConversationId))
                    .where(IMConversationDetailBeanDao.Properties.CurrentUid.eq(IMSNettyManager.getMyUseId()))
                    .orderAsc(IMConversationDetailBeanDao.Properties.Timestamp)
                    .list();
            return list;
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 删除某个userId下对应的所有message
     * @return
     */
    public void deleteConversationDetailAccordField(String ConversationId) {
        List<IMConversationDetailBean> beans = queryConversationDetailDataAccordField(ConversationId);
        if (beans == null) {
            return;
        }
        for (IMConversationDetailBean bean: beans) {
            deleteConversationDetailData(bean._id);
        }
    }

}
