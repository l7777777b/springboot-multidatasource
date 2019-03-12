package com.laurensius.springbootmultids.service;


import com.laurensius.springbootmultids.config.Constants;
import com.laurensius.springbootmultids.domain.User;
import com.laurensius.springbootmultids.repository.db1.UserDb1Repo;
import com.laurensius.springbootmultids.repository.db2.UserDb2Repo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DataSyncService {

    private final Logger log = LoggerFactory.getLogger(DataSyncService.class);

    private final ConfigurableEnvironment env;
    private final UserDb1Repo userDb1Repo;
    private final UserDb2Repo userDb2Repo;

    public DataSyncService(ConfigurableEnvironment env, UserDb1Repo userDb1Repo, UserDb2Repo userDb2Repo) {
        this.env = env;
        this.userDb1Repo = userDb1Repo;
        this.userDb2Repo = userDb2Repo;
    }

    public String syncData(){
        boolean enable = Boolean.valueOf(env.getProperty(Constants.ENV_SYNC_ENABLE));
        boolean db1todb2 = Boolean.valueOf(env.getProperty(Constants.ENV_SYNC_DB1TODB2));
        boolean db2todb1 = Boolean.valueOf(env.getProperty(Constants.ENV_SYNC_DB2TODB1));
        boolean userEnable = Boolean.valueOf(env.getProperty(Constants.ENV_SYNC_USER_ENABLE));
        int userPageSize = Integer.valueOf(env.getProperty(Constants.ENV_SYNC_USER_PAGESIZE));
        int userPage = Integer.valueOf(env.getProperty(Constants.ENV_SYNC_USER_PAGE));
        int userPageEnd = Integer.valueOf(env.getProperty(Constants.ENV_SYNC_USER_PAGEEND));

        //db1todb2 = true;
        //db2todb1 = false;
        //int pageSize = Integer.parseInt(env.getProperty(Constants.ENV_BATCH_SIZE));
        //int pageStart = Integer.parseInt(env.getProperty(Constants.IMPORT_EMPLOYEE_BANK_ASSOC_STARTPAGE));
        //int pageEnd = Integer.parseInt(env.getProperty(Constants.IMPORT_EMPLOYEE_BANK_ASSOC_ENDPAGE));
        //int pageSize = 0;
        //int page = 0;
        //int pageEnd = 0;
        //int resetPage = page;
        //int totalSize = 0;
        //int totalNew = 0;
        //int totalExist = 0;
        //int count = 0;
        //boolean dataExist;

        if (!enable) return "syncdata is disabled";
        if (db1todb2) {
            //db1 -> db2
            if (userEnable) {
                //user
                int pageSize = userPageSize;
                int page = userPage;
                int pageEnd = userPageEnd;
                log.debug("page: " + page);
                log.debug("pageEnd: " + pageEnd);
                int totalSize = 0;
                int totalNew;
                int totalExist;
                int count = 0;
                boolean dataExist = true;
                while (dataExist && (page < pageEnd || pageEnd == 0)) {
                    totalNew = 0;
                    totalExist = 0;
                    Pageable pageable = new PageRequest(page, pageSize);
                    Page<User> userDb1 = userDb1Repo.findAll(pageable);
                    log.info("userDb1 page: " + page + ", list size: " + userDb1.getContent().size());
                    List<User> newUserList = new ArrayList<>();
                    if (userDb1.getContent().size() > 0) {
                        totalSize += userDb1.getContent().size();
                        for (User user : userDb1.getContent()) {
                            User userDb2 = userDb2Repo.findOne(user.getId());
                            String msg = "no " + count + " userDb1 id: " + user.getId();
                            if (userDb2 == null) {
                                log.info(msg + " does not exist on db2, queued to be inserted...");
                                userDb2 = new User();
                                userDb2.setId(user.getId());
                                userDb2.setUsername(user.getUsername());
                                userDb2.setFullname(user.getFullname());
                                userDb2.setActive(user.getActive());
                                userDb2.setCreateTimestamp(user.getCreateTimestamp());
                                userDb2.setCreateTime(user.getCreateTime());
                                userDb2.setUpdateTime(user.getUpdateTime());
                                userDb2.setBirthdate(user.getBirthdate());
                                userDb2.setAddress(user.getAddress());
                                userDb2.setLongitude(user.getLongitude());
                                userDb2.setLatitude(user.getLatitude());
                                newUserList.add(userDb2);
                                totalNew++;
                            } else {
                                log.info(msg + " exists on db2, skip...");
                                totalExist++;
                            }
                            count++;
                        }
                        page++;
                    } else {
                        dataExist = false;
                    }
                    log.info("page " + page + " total new data will be inserted: " + totalNew + ", already exist: " + totalExist + ", out of " + totalSize);
                    userDb2Repo.save(newUserList);
                }
            }
        }
        if (db2todb1) {
            //db2 -> db1

            if (userEnable) {
                //user
                int pageSize = userPageSize;
                int page = userPage;
                int pageEnd = userPageEnd;
                log.debug("page: " + page);
                log.debug("pageEnd: " + pageEnd);
                int totalSize = 0;
                int totalNew;
                int totalExist;
                int count = 0;
                boolean dataExist = true;
                while (dataExist && (page < pageEnd || pageEnd == 0)) {
                    totalNew = 0;
                    totalExist = 0;
                    Pageable pageable = new PageRequest(page, pageSize);
                    Page<User> userDb2 = userDb2Repo.findAll(pageable);
                    log.info("userDb2 page: " + page + ", list size: " + userDb2.getContent().size());
                    List<User> newUserList = new ArrayList<>();
                    if (userDb2.getContent().size() > 0) {
                        totalSize += userDb2.getContent().size();
                        for (User user : userDb2.getContent()) {
                            User userDb1 = userDb1Repo.findOne(user.getId());
                            String msg = "no " + count + " userDb2 id: " + user.getId();
                            if (userDb1 == null) {
                                log.info(msg + " does not exist on db1, queued to be inserted...");
                                userDb1 = new User();
                                userDb1.setId(user.getId());
                                userDb1.setUsername(user.getUsername());
                                userDb1.setFullname(user.getFullname());
                                userDb1.setActive(user.getActive());
                                userDb1.setCreateTimestamp(user.getCreateTimestamp());
                                userDb1.setCreateTime(user.getCreateTime());
                                userDb1.setUpdateTime(user.getUpdateTime());
                                userDb1.setBirthdate(user.getBirthdate());
                                userDb1.setAddress(user.getAddress());
                                userDb1.setLongitude(user.getLongitude());
                                userDb1.setLatitude(user.getLatitude());
                                newUserList.add(userDb1);
                                totalNew++;
                            } else {
                                log.info(msg + " exists on db1, skip...");
                                totalExist++;
                            }
                            count++;
                        }
                        page++;
                    } else {
                        dataExist = false;
                    }
                    log.info("page " + page + " total new data will be inserted: " + totalNew + ", already exist: " + totalExist + ", out of " + totalSize);
                    userDb1Repo.save(newUserList);
                }
            }
        }


        return "done";
    }

}
