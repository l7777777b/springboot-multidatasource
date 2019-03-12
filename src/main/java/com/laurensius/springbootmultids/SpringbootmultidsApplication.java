package com.laurensius.springbootmultids;

import com.laurensius.springbootmultids.config.Constants;
import com.laurensius.springbootmultids.repository.db1.UserDb1Repo;
import com.laurensius.springbootmultids.repository.db2.UserDb2Repo;
import com.laurensius.springbootmultids.service.DataSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
public class DatasyncApplication implements CommandLineRunner {

	private static Logger log = LoggerFactory.getLogger(DatasyncApplication.class);

    private final ConfigurableEnvironment env;
    private final UserDb1Repo userDb1Repo;
    private final UserDb2Repo userDb2Repo;
    private final DataSyncService dataSyncService;

    public DatasyncApplication(ConfigurableEnvironment env, UserDb1Repo userDb1Repo, UserDb2Repo userDb2Repo, DataSyncService dataSyncService) {
        this.env = env;
        this.userDb1Repo = userDb1Repo;
        this.userDb2Repo = userDb2Repo;
        this.dataSyncService = dataSyncService;
    }


    public static void main(String[] args) {
		SpringApplication.run(DatasyncApplication.class, args);
	}

    @Override
    public void run(String... strings) throws Exception {
        boolean syncEnable = Boolean.valueOf(env.getProperty(Constants.ENV_SYNC_ENABLE));
        boolean db1todb2 = Boolean.valueOf(env.getProperty(Constants.ENV_SYNC_DB1TODB2));
        boolean db2todb1 = Boolean.valueOf(env.getProperty(Constants.ENV_SYNC_DB2TODB1));
        boolean userEnable = Boolean.valueOf(env.getProperty(Constants.ENV_SYNC_USER_ENABLE));
        int userPageSize = Integer.valueOf(env.getProperty(Constants.ENV_SYNC_USER_PAGESIZE));
        int userPage = Integer.valueOf(env.getProperty(Constants.ENV_SYNC_USER_PAGE));
        int userPageEnd = Integer.valueOf(env.getProperty(Constants.ENV_SYNC_USER_PAGEEND));
        log.info(Constants.ENV_SYNC_ENABLE+": "+syncEnable);
        log.info(Constants.ENV_SYNC_DB1TODB2+": "+db1todb2);
        log.info(Constants.ENV_SYNC_DB2TODB1+": "+db2todb1);
        log.info(Constants.ENV_SYNC_USER_ENABLE+": "+userEnable);
        log.info(Constants.ENV_SYNC_USER_PAGESIZE+": "+userPageSize);
        log.info(Constants.ENV_SYNC_USER_PAGE+": "+userPage);
        log.info(Constants.ENV_SYNC_USER_PAGEEND+": "+userPageEnd);

        if (syncEnable) {
            Long userDb1Count = userDb1Repo.count();
            Long userDb2Count = userDb2Repo.count();
            log.info("total size user db 1: " + userDb1Count);
            log.info("total size user db 2: " + userDb2Count);
            dataSyncService.syncData();
        }
    }
}
