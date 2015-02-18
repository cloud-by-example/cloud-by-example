/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cloudbyexample.dc.service.si.notification;

import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

/**
 * Sends an e-mail using a velocity template for the body.
 *
 * @author David Winterfeldt
 */
@Component
public class VelocityEmailSender implements Sender {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private final VelocityEngine velocityEngine;
    private final JavaMailSender mailSender;

    @Autowired
    public VelocityEmailSender(VelocityEngine velocityEngine,
                               JavaMailSender mailSender) {
        this.velocityEngine = velocityEngine;
        this.mailSender = mailSender;
    }

    @Override
    public void send(final SimpleMailMessage msg,
                     final String templatePath,
                     final Map<String, Object> vars) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
               MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
               message.setTo(msg.getTo());
               message.setFrom(msg.getFrom());
               message.setSubject(msg.getSubject());

               String body = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templatePath, "UTF-8", vars);

               logger.debug("body={}", body);

               message.setText(body, true);
            }
         };

         mailSender.send(preparator);

        logger.info("Sent e-mail to '{}'.  template='{}'", msg.getTo(), templatePath);
    }

}