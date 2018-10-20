package com.yan.search.service;

import com.yan.common.pojo.SearchResult;

import java.io.IOException;

public interface SearchService {
    SearchResult search(String keyword,int page,int rows) throws Exception;

}
