package com.hehe.user.verification.event;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.eventbus.Subscribe;
import com.hehe.common.event.EventListener;
import com.hehe.common.util.ServiceException;
import com.hehe.common.util.VerifyUtil;
import com.hehe.user.verification.model.Email;
import com.hehe.user.verification.model.Sms;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author xieqinghe .
 * @date 2018/3/13 上午10:52
 * @email xieqinghe@terminus.io
 */
@Slf4j
@Component
public class SendEvent implements EventListener {

    @Value("${verification.email.host}")
    private String host;
    @Value("${verification.email.username}")
    private String username;
    @Value("${verification.email.password}")
    private String password;
    @Value("${verification.email.timeout}")
    private Integer timeout = 25000;

    @Value("${verification.sms.appId}")
    private Integer appId;
    @Value("${verification.sms.appKey}")
    private String appKey;


    /**
     * 发送邮件
     */
    @Subscribe
    public void sendMimeMail(Email mail) {
        try {
            checkNotNull(host, "email host not null");
            checkNotNull(username, "email username not null");
            checkNotNull(password, "email password not null");
            if (!VerifyUtil.verifyEmail(mail.getToAddress())) {
                throw new ServiceException("user.email.not.legal");
            }

            JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
            // 设定mail server
            javaMailSender.setHost(host);
            javaMailSender.setUsername(username);
            javaMailSender.setPassword(password);

            Properties prop = new Properties();
            //设为true，让服务器进行认证,认证用户名和密码是否正确
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.timeout", timeout);
            prop.put("mail.smtp.starttls.enable", true);
            prop.put("mail.smtp.socketFactory.port", "25");
            prop.put("mail.smtp.socketFactory.fallback", "false");
            javaMailSender.setJavaMailProperties(prop);

            // 建立邮件消息
            MimeMessageHelper mailMessage = new MimeMessageHelper(javaMailSender.createMimeMessage(), Charsets.UTF_8.toString());
            //发件人
            mailMessage.setFrom(mail.getFromAddress());
            //收件人
            mailMessage.setTo(mail.getToAddress());
            //标题
            mailMessage.setSubject(mail.getSubject());
            //内容
            mailMessage.setText(mail.getContent());

            // 发送邮件
            javaMailSender.send(mailMessage.getMimeMessage());

            log.info("send email success, mail:{}", mail);

        } catch (Exception e) {
            log.error("send email fail, mail={}, cause:{}", mail, Throwables.getStackTraceAsString(e));
        }
    }


    /**
     * 发送短信(腾讯云接口)
     */
    @Subscribe
    public void sendSms(Sms sms) {
        try {
            checkNotNull(appId, "sms appId not null");
            checkNotNull(appKey, "sms appKey not null");
            checkNotNull(sms.getContent(), "sms content not null");
            checkNotNull(sms.getToPhone(), "sms toPhone not null");
            if (!VerifyUtil.verifyMobile(sms.getToPhone())) {
                throw new ServiceException("user.phone.not.legal");
            }

            SmsSingleSender sender = new SmsSingleSender(appId, appKey);

            //如需发送海外短信，同样可以使用此接口，只需将国家码 86 改写成对应国家码号
            SmsSingleSenderResult result = sender.send(
                    0, "86",
                    sms.getToPhone(), sms.getContent(),
                    "", "");
            log.info("send sms success, sms: {}, result:{}", sms, result);
        } catch (HTTPException e) {
            // HTTP响应码错误
            log.error("send email fail, sms: {}, cause:{}", sms, Throwables.getStackTraceAsString(e));
            e.printStackTrace();
        } catch (JSONException e) {
            // json解析错误
            log.error("send email fail, sms: {}, cause:{}", sms, Throwables.getStackTraceAsString(e));
            e.printStackTrace();
        } catch (IOException e) {
            // 网络IO错误
            log.error("send email fail, sms: {}, cause:{}", sms, Throwables.getStackTraceAsString(e));
            e.printStackTrace();
        }

    }

}
