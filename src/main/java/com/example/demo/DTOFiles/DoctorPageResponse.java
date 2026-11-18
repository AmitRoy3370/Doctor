package com.example.demo.DTOFiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DoctorPageResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 10L;

	private List<DoctorResponse> responses = new ArrayList<>();

	private int page;
	private int size;
	private long totalElements;
	private String nextPage;
	private String previousPage;

	public DoctorPageResponse(List<DoctorResponse> responses, int page, int size, long totalElements, String nextPage,
			String previousPage) {
		super();
		this.responses = responses;
		this.page = page;
		this.size = size;
		this.totalElements = totalElements;
		this.nextPage = nextPage;
		this.previousPage = previousPage;
	}

	public DoctorPageResponse() {
		super();
	}

	public List<DoctorResponse> getResponses() {
		return responses;
	}

	public void setResponses(List<DoctorResponse> responses) {
		this.responses = responses;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public String getNextPage() {
		return nextPage;
	}

	public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}

	public String getPreviousPage() {
		return previousPage;
	}

	public void setPreviousPage(String previousPage) {
		this.previousPage = previousPage;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "DoctorPageResponse [responses=" + responses + ", page=" + page + ", size=" + size + ", totalElements="
				+ totalElements + ", nextPage=" + nextPage + ", previousPage=" + previousPage + "]";
	}
}
