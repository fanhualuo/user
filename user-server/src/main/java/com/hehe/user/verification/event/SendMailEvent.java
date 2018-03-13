package com.hehe.user.verification.event;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.eventbus.Subscribe;
import com.hehe.common.event.EventListener;
import com.hehe.user.verification.model.Email;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.Properties;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author xieqinghe .
 * @date 2018/3/13 上午10:52
 * @email xieqinghe@terminus.io
 */
@Slf4j
@Component
@ConfigurationProperties(prefix = "email")
public class SendMailEvent implements EventListener {

    @Setter
    private String host;
    @Setter
    private String username;
    @Setter
    private String password;
    @Setter
    private Integer timeout = 25000;

    /**
     * 发送邮件
     */
    @Subscribe
    public void sendMimeMail(Email mail) {
        try {
            checkNotNull(host, "email host not null");
            checkNotNull(username, "email username not null");
            checkNotNull(password, "email password not null");
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
            MimeMessageHelper mailMessage = new MimeMessageHelper(javaMailSender.createMimeMessage(),  Charsets.UTF_8.toString());

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
            log.error("send email fail, mail={}, cause:{}",
                    mail, Throwables.getStackTraceAsString(e));
        }
    }
}
