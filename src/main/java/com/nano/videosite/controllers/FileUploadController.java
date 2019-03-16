package com.nano.videosite.controllers;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nano.videosite.models.Video;
import com.nano.videosite.models.ViewVideo;
import com.nano.videosite.services.ViewService;
//import com.nano.videosite.repositories.VideoRepository;
import com.nano.videosite.storage.StorageFileNotFoundException;
import com.nano.videosite.storage.StorageService;

@RestController
public class FileUploadController {

    private final StorageService storageService;
    

//    @Autowired
//    private VideoRepository videoRepository;
    
    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }
    
//    @GetMapping("/")
    @RequestMapping(method= RequestMethod.GET, value = "/", produces={MediaType.APPLICATION_JSON_VALUE})
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

//    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    @RequestMapping(method= RequestMethod.GET, value = "/files/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename, HttpServletResponse res) {

        Resource file = storageService.loadAsResource(filename);
        res.setHeader("Content-Type", "video/mp4");
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

//    @PostMapping("/")
    @RequestMapping(method= RequestMethod.POST, value = "/", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Video> handleFileUpload(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {

        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");
//        return "redirect:/";
        Video video = new Video(file.getOriginalFilename());
//        Video test = videoRepository.save(video);
//        System.out.println(test.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(video);
    }
    


    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
