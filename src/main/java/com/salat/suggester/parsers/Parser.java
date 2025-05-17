package com.salat.suggester.parsers;

import com.salat.suggester.BugEntity;

import java.io.File;
import java.util.List;

public interface Parser {
    List<BugEntity> parse(File bugsReportFile) throws Exception;
}
