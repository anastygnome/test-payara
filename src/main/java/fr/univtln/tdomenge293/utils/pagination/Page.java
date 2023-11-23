package fr.univtln.tdomenge293.utils.pagination;

import java.util.List;

public record Page<T>(int pageNumber, int pageSize, List<T> result) { }
