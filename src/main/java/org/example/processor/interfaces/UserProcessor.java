package org.example.processor.interfaces;

import java.io.File;
import java.util.List;

import org.example.model.User;

public interface UserProcessor {
    List<User> processFiles(File[] files) throws Exception;
}
