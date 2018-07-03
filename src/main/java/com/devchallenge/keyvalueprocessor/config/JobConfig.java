package com.devchallenge.keyvalueprocessor.config;


import com.devchallenge.keyvalueprocessor.helper.NodeSyncErrorHandlingJob;
import org.quartz.Trigger;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

@Configuration
public class JobConfig {

    @Value("${error.handling.cron.expression}")
    private String cronExpression;


    @Bean
    public AutowiringSpringBeanJobFactory autowiringSpringBeanJobFactory() {
        return new AutowiringSpringBeanJobFactory();
    }


    @Bean
    public SchedulerFactoryBean job() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(autowiringSpringBeanJobFactory());
        Trigger trigger = cronTrigger().getObject();
        schedulerFactoryBean.setTriggers(trigger);
        return schedulerFactoryBean;
    }

    @Bean
    protected CronTriggerFactoryBean cronTrigger() {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setJobDetail(jobFactory().getObject());
        cronTriggerFactoryBean.setCronExpression(cronExpression);
        return cronTriggerFactoryBean;
    }

    @Bean
    protected JobDetailFactoryBean jobFactory() {
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        jobDetailFactoryBean.setJobClass(NodeSyncErrorHandlingJob.class);
        jobDetailFactoryBean.setDurability(true);
        return jobDetailFactoryBean;
    }


    public static class AutowiringSpringBeanJobFactory
            extends SpringBeanJobFactory
            implements ApplicationContextAware {

        private transient AutowireCapableBeanFactory beanFactory;

        public void setApplicationContext(
                final ApplicationContext context) {
            beanFactory = context.getAutowireCapableBeanFactory();
        }

        @Override
        protected Object createJobInstance(
                final TriggerFiredBundle bundle)
                throws Exception {
            final Object job = super.createJobInstance(bundle);
            beanFactory.autowireBean(job);
            return job;
        }
    }
}

