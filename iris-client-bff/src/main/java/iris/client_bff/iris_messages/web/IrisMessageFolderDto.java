package iris.client_bff.iris_messages.web;

import iris.client_bff.iris_messages.IrisMessageContext;
import iris.client_bff.iris_messages.IrisMessageFolder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class IrisMessageFolderDto {

    private String id;
    private String name;
    private IrisMessageContext context;
    private List<IrisMessageFolderDto> items;
    private Boolean isDefault;

    private static IrisMessageFolderDto findParentFolderDto(List<IrisMessageFolderDto> folderDtoList, IrisMessageFolder folder) {
        if (folder.getParentFolder() == null) {
            return null;
        }
        for ( IrisMessageFolderDto folderDto : folderDtoList ) {
            if(folderDto.getId().equals(folder.getParentFolder().toString())) {
                return folderDto;
            }
            if(!folderDto.items.isEmpty()) {
                IrisMessageFolderDto parentFolderDto = IrisMessageFolderDto.findParentFolderDto(folderDto.items, folder);
                if (parentFolderDto != null) {
                    return parentFolderDto;
                }
            }
        }
        return null;
    }

    public static IrisMessageFolderDto fromEntity(IrisMessageFolder folder) {
        return new IrisMessageFolderDto()
                .setId(folder.getId().toString())
                .setName(folder.getName())
                .setContext(folder.getContext())
                .setIsDefault(folder.getIsDefault())
                .setItems(new ArrayList<>());
    }

    public static List<IrisMessageFolderDto> fromEntity(List<IrisMessageFolder> folderList) {
        List<IrisMessageFolderDto> folderDtoList = new ArrayList<>();
        for ( IrisMessageFolder folder : folderList ) {
            IrisMessageFolderDto folderDto = IrisMessageFolderDto.fromEntity(folder);
            IrisMessageFolderDto parentFolderDto = IrisMessageFolderDto.findParentFolderDto(folderDtoList, folder);
            if (parentFolderDto != null) {
                parentFolderDto.items.add(folderDto);
            } else {
                folderDtoList.add( folderDto );
            }
        }
        return folderDtoList;
    }

}
