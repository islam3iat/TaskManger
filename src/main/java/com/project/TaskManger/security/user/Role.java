package com.project.TaskManger.security.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.project.TaskManger.security.user.Permission.*;

@RequiredArgsConstructor
public enum Role {

  USER(Collections.emptySet()),
  ADMIN(Set.of(ADMIN_READ
          ,ADMIN_CREATE
          ,ADMIN_DELETE
          ,ADMIN_PUT));

  @Getter
  private final Set<Permission> userPermission;
  public List<SimpleGrantedAuthority> getAuthority(){
    List<SimpleGrantedAuthority> authority = getUserPermission()
            .stream().
            map(permission -> new SimpleGrantedAuthority(permission.getPermission())).
            collect(Collectors.toList());
    authority.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
    return authority;
  }
}
