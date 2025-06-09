package model;

import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

public class MultipartRequest {

    @RestForm("file")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public FileUpload file;

    @RestForm("fileName")
    @PartType(MediaType.TEXT_PLAIN)
    public String fileName;
}
