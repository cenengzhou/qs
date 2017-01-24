/**
 * GammonQS-PH3
 * UpdateSCDetailNewQuantityWrapper.java
 * @author tikywong
 * created on Nov 6, 2012 9:29:58 AM
 * 
 */
package com.gammon.qs.wrapper.splitTerminateSC;

import java.io.Serializable;

public class UpdateSCDetailNewQuantityWrapper implements Serializable{
	
	private static final long serialVersionUID = -3749781633776442087L;
	private Long id;
	private Double newQuantity;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Double getNewQuantity() {
		return newQuantity;
	}
	public void setNewQuantity(Double newQuantity) {
		this.newQuantity = newQuantity;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
