package com.laurensius.springbootmultids.repository.db2;

import com.laurensius.springbootmultids.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDb2Repo extends JpaRepository<User, Long> {

}