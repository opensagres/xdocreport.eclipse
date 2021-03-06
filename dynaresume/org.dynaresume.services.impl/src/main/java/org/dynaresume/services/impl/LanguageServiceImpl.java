package org.dynaresume.services.impl;

import org.dynaresume.dao.LanguageDao;
import org.dynaresume.domain.hr.Language;
import org.dynaresume.services.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("languageService")
public class LanguageServiceImpl implements LanguageService {

	@Autowired
	private LanguageDao languageDao;

	public Iterable<Language> findAll() {
		return languageDao.findAll();
	}

	public Language findById(long id) {
		return languageDao.findOne(id);
	}

	public Language save(Language language) {
		return languageDao.save(language);
	}

//	public Page<Language> findByName(String name, Pageable pageable) {
//		return languageDao.findByNameLike(name, pageable);
//	}
}
