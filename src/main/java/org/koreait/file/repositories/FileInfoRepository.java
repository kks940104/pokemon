package org.koreait.file.repositories;

import org.koreait.file.entites.FileInfo;
import org.koreait.file.entites.QFileInfo;
import org.koreait.member.entities.QMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface FileInfoRepository extends JpaRepository<FileInfo, Long>, QuerydslPredicateExecutor<FileInfo> {

}
