package com.pwm.dev.service;

import com.pwm.dev.mapper.TestMapper;
import com.pwm.dev.po.TestTablePo;
import com.pwm.dev.repo.TestTableRepo;
import com.pwm.dev.utils.GetUUID;
import com.pwm.dev.utils.PageResponse;
import com.pwm.dev.vo.QueryByUserIdInVo;
import com.pwm.dev.vo.QueryByUserIdOutVo;
import com.pwm.dev.vo.QueryListByPageInVo;
import com.pwm.dev.vo.QueryListByPageOutVo;
import com.pwm.dev.vo.QueryListInVo;
import com.pwm.dev.vo.QueryListOutVo;
import com.pwm.dev.vo.TestInVo;
import com.pwm.dev.vo.TestTableVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Component
public class TestServiceImpl implements TestService{

    @Autowired
    RedisTemplate<Object,Object> redisTemplate;

    @Autowired
    TestMapper testMapper;

    @Autowired
    TestTableRepo testTableRepo;

    @Override
    public void getRedisKey(TestInVo inVo) {
        //返回当前缓存的VALUE
        String returnValue = (String) redisTemplate.opsForValue().get(inVo.getName());
        log.info("redis_key："+inVo.getName()+",redis_value："+returnValue);

        //设置当前KEY的值并返回设置前的旧值
        String returnValue1 = (String) redisTemplate.opsForValue().getAndSet(inVo.getName(),"pengweiming1");
        log.info("redis_key："+inVo.getName()+",redis_value："+returnValue1);

        //获取当前缓存最新的VALUE
        String returnValue2 = (String) redisTemplate.opsForValue().get(inVo.getName());
        log.info("redis_key："+inVo.getName()+",redis_value："+returnValue2);

    }

    @Override
    public void setRedisKey(TestInVo inVo) {
        if(redisTemplate.opsForValue().get(inVo.getName())==null){
            //如果这个KEY不在缓存里，就设置一个值
            redisTemplate.opsForValue().set(inVo.getName(),"pengweiming");
            log.info("写入redis key 成功");
        }
    }

    @Override
    public QueryListOutVo queryList(QueryListInVo inVo) {
        QueryListOutVo outVo=new QueryListOutVo();
        Map map=new HashMap<>();
        map.put("userName",inVo.getUserName());
        List<TestTablePo> list=testMapper.queryList(map);
        List<TestTableVo> returnList= new ArrayList<>();

        if(!CollectionUtils.isEmpty(list)){
            for(TestTablePo po:list){
                TestTableVo vo=new TestTableVo();
                BeanUtils.copyProperties(po,vo);
                returnList.add(vo);
            }
        }

        outVo.setList(returnList);
        log.info("queryList success!");
        return outVo;
    }

    @Override
    public QueryListByPageOutVo queryListByPage(QueryListByPageInVo inVo) {
        QueryListByPageOutVo outVo=new QueryListByPageOutVo();

        //注意：Spring data jpa 默认 currentPage从0开始，所以需要对传入的页码参数-1
        Pageable pageable=PageRequest.of(inVo.getCurrentPage()-1,inVo.getPageSize());
        Page<TestTablePo> pageList=testTableRepo.findAll(pageable);

        PageResponse pageResponse=new PageResponse();
        pageResponse.setPageSize(pageList.getPageable().getPageSize());

        //注意：Spring data jpa 默认 currentPage从0开始，所以需要对返回的页码参数+1
        pageResponse.setCurrentPage(pageList.getPageable().getPageNumber()+1);
        pageResponse.setTotalPages(pageList.getTotalPages());
        pageResponse.setTotalRecords((int)pageList.getTotalElements());
        pageResponse.setData(pageList.getContent());
        outVo.setPageResponse(pageResponse);
        return outVo;
    }

    @Override
    public QueryByUserIdOutVo queryByUserId(QueryByUserIdInVo inVo) {
        QueryByUserIdOutVo outVo=new QueryByUserIdOutVo();
        TestTablePo po=testTableRepo.queryByUserId(inVo.getUserId()).orElse(null);
        TestTableVo vo=new TestTableVo();
        BeanUtils.copyProperties(po,vo);
        outVo.setVo(vo);
        return outVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(TestTableVo inVo) {
        TestTablePo po=new TestTablePo();
        BeanUtils.copyProperties(inVo,po);
        po.setId(GetUUID.getUUID());
        testTableRepo.save(po);
        log.info("save success!");
    }

    @Override
    public void sendEmail() {
        try{
            Session session=createEmailSession();

            MimeMessage msg=new MimeMessage(session);
            msg.setFrom(new InternetAddress("pengweiming3320@sina.com"));//发件人
            msg.setRecipient(Message.RecipientType.TO,new InternetAddress("pengweiming3320@sina.com"));//收件人
            msg.setSubject("测试邮件","utf-8");//邮件标题


            //发送内容
            //文本内容
            MimeBodyPart textPart=new MimeBodyPart();

            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String nowTime=sdf.format(new Date());

            StringBuffer sb=new StringBuffer();

            String textContent="这是通过java代码生成的一个测试邮件信息！发送时间为："+nowTime;
            sb.append(textContent);

            sb.append("<br/>");

            String imgContent="<img src='cid:testImg' />";
            sb.append(imgContent);

            textPart.setContent(sb.toString(),"text/html;charset=utf-8");


            //图片内容
            MimeBodyPart imgPart=new MimeBodyPart();

            //附件名称如果是中文需要用到MimeUtility.encodeText,不然是乱码
            imgPart.setFileName(MimeUtility.encodeText("狗熊.jpg"));
            String imgUrl="C:\\Users\\PWM\\Desktop\\狗熊.jpg";
            imgPart.setDataHandler(new DataHandler(new FileDataSource(imgUrl)));
            imgPart.setContentID("testImg");

            Multipart multipart=new MimeMultipart();
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(imgPart);

            msg.setContent(multipart);

            Transport.send(msg);
            log.info("邮件发送完毕!");

        }catch (Exception e){
            log.error("send email error!!!",e);
            e.printStackTrace();
        }
    }

    public Session createEmailSession(){
        //新浪邮箱服务器地址
        String smtp="smtp.sina.com";

        //邮箱账号
        String emailUserName="pengweiming3320@sina.com";

        //授权密码（在邮箱里设置获取）
        String emailPassWord="c4893fb1a762feca";

        //SMTP服务器信息
        Properties pro=new Properties();
        pro.put("mail.smtp.host",smtp);//主机名
        pro.put("mail.smtp.port",25);//端口
        pro.put("mail.smtp.auth","true");//是否需要用户认证

        //创建Session
        Session session=Session.getInstance(pro, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailUserName,emailPassWord);
            }
        });

        //开启调试模式
        session.setDebug(true);
        return session;
    }


    public static void main(String[] args) throws Exception {
        //查看堆内存的反应和内存回收情况
        System.out.println("开始");

        Thread.sleep(20000);//20秒

        System.out.println("准备创建对象");
        byte[] array=new byte[1024*1024*10];//10M的对象
        System.out.println("创建10M的对象完毕");

        Thread.sleep(20000);

        System.out.println("开始回收内存");
        array=null;
        System.gc();
        System.out.println("回收内存结束");

        Thread.sleep(20000);
        System.out.println("结束");

    }

}
