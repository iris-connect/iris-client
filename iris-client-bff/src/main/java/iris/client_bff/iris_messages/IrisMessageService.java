package iris.client_bff.iris_messages;

import iris.client_bff.cases.CaseDataRequest;
import iris.client_bff.events.EventDataRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IrisMessageService {

    private final IrisMessageRepository repository;
    private final IrisMessageFolderRepository folderRepository;

    public Page<IrisMessage> findAllByFolderId(String folder, Pageable pageable) {
        var id = IrisMessageFolder.IrisMessageFolderIdentifier.of(folder);
        return repository.findAllByFolderId(id, pageable);
    }

    public int getCountUnread(String folder) {
        return repository.getCountUnread(folder);
    }

    public List<IrisMessageFolder> getFolders() {
        return folderRepository.findAll();
    }
}
