package com.app.positionback.service.corporation;

import com.app.positionback.domain.apply.ApplyDTO;
import com.app.positionback.domain.apply.ApplyListDTO;
import com.app.positionback.domain.corporation.CorporationVO;
import com.app.positionback.domain.file.FileDTO;
import com.app.positionback.domain.member.MemberDTO;
import com.app.positionback.repository.corporation.CorporationDAO;
import com.app.positionback.repository.file.CorporationFileDAO;
import com.app.positionback.repository.file.FileDAO;
import com.app.positionback.repository.member.MemberDAO;
import com.app.positionback.utill.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Primary
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CorporationServiceImpl implements CorporationService {
    private final CorporationDAO corporationDAO;
    private final CorporationFileDAO corporationFileDAO;
    private final FileDAO fileDAO;

    @Override
    public void join(CorporationVO corporationVO) {
        corporationDAO.save(corporationVO);
    }

    @Override
    public Long getLastInsertId() {
        return corporationDAO.findLastInsertId();
    }

    @Override
    public int checkCorporationEmail(String corporationEmail) {
        return corporationDAO.findCountByCorporationEmail(corporationEmail);
    }

    @Override
    public Optional<CorporationVO> login(MemberDTO memberDTO) {
        return corporationDAO.findByCorporationEmailAndCorporationPassword(memberDTO);
    }

    @Override
    public FileDTO getCorporationFileById(Long corporationId) {
        Long fileId = corporationFileDAO.getFileIdByCorporationId(corporationId);
        return fileDAO.findById(fileId);
    }

    @Override
    public ApplyListDTO getApplyByCorporationId(int page, Pagination pagination, Long corporationId) {
        ApplyListDTO applyListDTO = new ApplyListDTO();
        pagination.setPage(page);
        pagination.setTotal(corporationDAO.getTotal(pagination,corporationId));
        pagination.progress();
        applyListDTO.setPagination(pagination);
        applyListDTO.setApplies(corporationDAO.findApplyByCorporationId(pagination,corporationId));


        return applyListDTO;
    }

    @Override
    public int getTotal(Pagination pagination, Long corporationId) {
        return corporationDAO.getTotal(pagination,corporationId);
    }
}
