package it.develhope.fileuploadAndDownload.uploadAndDownload.services;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${fileRepositoryFolder}")
    private String fileRepositoryFolder;

    public String upload(MultipartFile file)throws IOException {
        //Questa stringa, con UUID.randomUUID(), modifica il nome originale del file con uno generato random
        String newNameFile = UUID.randomUUID().toString();
        //Questa stringa, estrapola l'estensione del file originale
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        //Qui ritorna il nome completo, composto dalla stringa generata random più la sua estensione
        String completeFileName = newNameFile + "." + extension;
        File finalFolder = new File(fileRepositoryFolder);
        if(!finalFolder.exists()) throw new IOException("Final folder does not exists");
        if(!finalFolder.isDirectory()) throw new IOException("Final folder is not a directory");
        File finalDestination = new File(fileRepositoryFolder + "\\" + completeFileName);
        if(finalDestination.exists()) throw new IOException("File conflict");
        file.transferTo(finalDestination);
        return completeFileName;
    }

    public byte[] download(String fileName) throws Exception{
            File fileFromRepository = new File(fileRepositoryFolder + "\\" + fileName);
                if (!fileFromRepository.exists())throw new IOException("File doesn't exists");
                return IOUtils.toByteArray(new FileInputStream(fileFromRepository));
    }


}

