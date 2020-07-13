package com.azkz.businesslogic.service;

import com.azkz.common.KashiteConst;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

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
		String name = (String) principal.getAttributes().get("name");

		boolean existsFlg = mstUserRepository.existsBySubjectAndIssuer(sub, iss);

		// ユーザーマスタに存在していたら、初期処理を行わない
		if (existsFlg) {
			return;
		}

		// 名前がnullの場合
		if (StringUtils.isBlank(name)) {
			name = "no name";
		}

		// 登録処理
		MstUser mstUser = new MstUser(sub, iss, KashiteConst.ENABLE_FLG_ENABLE, name, "azegami", "initialLogin");
		mstUserRepository.saveAndFlush(mstUser);

	}

	public long getUserId(OidcUser principal) {

		String sub = principal.getSubject();
		String iss = principal.getIssuer().toString();

		MstUser mstUser = mstUserRepository.findBySubjectAndIssuer(sub, iss);

		if (mstUser == null) {
			throw new IllegalArgumentException("対象のユーザーが存在しません");
		}

		return mstUser.getId();

	}

}
