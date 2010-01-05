/* *********************************************************************** *
 * project: org.matsim.*
 * GraphProjection.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2007 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */

/**
 * 
 */
package org.matsim.contrib.sna.graph;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * This class allows to extract subgrimport org.matsim.contrib.sna.graph.EdgeDecorator;
aphs from existing graphs by projecting
 * specific vertices and edges onto a new graph. A GraphProjection works similar
 * to a decorator, however, the connectivity of vertices and edgeimport org.matsim.contrib.sna.graph.VertexDecorator;
s can differ
 * from the original graph.
 * 
 * @author illenberger
 * 
 */
public class GraphProjection<G extends Graph, V extends Vertex, E extends Edge> extends SparseGraph {

	private G delegate;
	
	private Map<V, VertexDecorator<V>> vMapping = new HashMap<V, VertexDecorator<V>>();
	
	/**
	 * Creates a new empty projection from graph <tt>delegate</tt>.
	 * @param delegate the original graph.
	 */
	public GraphProjection(G delegate) {
		this.delegate = delegate;
	}
	
	/**
	 * Returns the original graph.
	 * @return the original graph.
	 */
	public G getDelegate() {
		return delegate;
	}
	
	/**
	 * Returns the decorator representing the projection of vertex <tt>v</tt>.
	 * @param v the original vertex.
	 * @return the decorater of vertex <tt>v</tt>, or <tt>null</tt> if there is no projection fot <tt>v</tt>.
	 */
	public VertexDecorator<V> getVertex(V v) {
		return vMapping.get(v);
	}

	/**
	 * Associates vertex <tt>delegate</tt> with its decorator <tt>decorator</tt>.
	 * @param delegate the original vertex.
	 * @param decorator the decorator of the original vertex.
	 */
	void setMapping(V delegate, VertexDecorator<V> decorator) {
		vMapping.put(delegate, decorator);
	}

	/**
	 * @see {@link Graph#getEdges()}.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Set<? extends EdgeDecorator<E>> getEdges() {
		return (Set<? extends EdgeDecorator<E>>) super.getEdges();
	}

	/**
	 * @see {@link Graph#getVertices()}.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Set<? extends VertexDecorator<V>> getVertices() {
		return (Set<? extends VertexDecorator<V>>) super.getVertices();
	}

	/** 
	 * @see org.matsim.contrib.sna.graph.SparseGraph#getEdge(org.matsim.contrib.sna.graph.SparseVertex, org.matsim.contrib.sna.graph.SparseVertex)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public EdgeDecorator<E> getEdge(SparseVertex v_i, SparseVertex v_j) {
		return (EdgeDecorator<E>) super.getEdge(v_i, v_j);
	}
}
