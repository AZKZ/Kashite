package com.azkz.businesslogic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.azkz.businesslogic.repository.MstUserRepository;
import com.azkz.infrastructure.entity.MstUser;

@Service
public class UserService {

	private final MstUserRepository mstUserRepository;

	@Autowired
	public UserService(MstUserRepository mstUserRepository) {
		this.mstUserRepository = mstUserRepository;
	}

	public void initialLogin(OidcUser principal) {

		String sub = principal.getSubject();
		String iss = principal.getIssuer().toString();
		String name = principal.getAttribute("name");

		boolean existsFlg = mstUserRepository.existsBySubjectAndIssuer(sub, iss);

		// ユーザーマスタに存在していたら、初期処理を行わない
		if (existsFlg) {
			return;
		}

		// 名前がnullの場合
		if (StringUtils.isEmpty(name)) {
			name = "untitled";
		}

		// 登録処理
		MstUser mstUser = new MstUser(sub, iss, "1", name, "azegami", "initialLogin");
		mstUserRepository.saveAndFlush(mstUser);

	}

	public long getUserId(OidcUser principal) {

		String sub = principal.getSubject();
		String iss = principal.getIssuer().toString();

		MstUser mstUser = mstUserRepository.findBySubjectAndIssuer(sub, iss);

		if (mstUser == null) {
			return -1;
		}

		return mstUser.getId();

	}

}
