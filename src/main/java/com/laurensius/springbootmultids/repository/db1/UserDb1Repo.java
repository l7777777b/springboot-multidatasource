package com.laurensius.springbootmultids.repository.db1;

import com.laurensius.springbootmultids.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDb1Repo extends JpaRepository<User, Long> {

}