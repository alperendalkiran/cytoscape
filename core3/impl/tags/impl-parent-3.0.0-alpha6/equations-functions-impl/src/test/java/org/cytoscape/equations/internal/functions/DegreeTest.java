/*
  File: DegreeTest.java

  Copyright (c) 2010, The Cytoscape Consortium (www.cytoscape.org)

  This library is free software; you can redistribute it and/or modify it
  under the terms of the GNU Lesser General Public License as published
  by the Free Software Foundation; either version 2.1 of the License, or
  any later version.

  This library is distributed in the hope that it will be useful, but
  WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF
  MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.  The software and
  documentation provided hereunder is on an "as is" basis, and the
  Institute for Systems Biology and the Whitehead Institute
  have no obligations to provide maintenance, support,
  updates, enhancements or modifications.  In no event shall the
  Institute for Systems Biology and the Whitehead Institute
  be liable to any party for direct, indirect, special,
  incidental or consequential damages, including lost profits, arising
  out of the use of this software and its documentation, even if the
  Institute for Systems Biology and the Whitehead Institute
  have been advised of the possibility of such damage.  See
  the GNU Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with this library; if not, write to the Free Software Foundation,
  Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
*/
package org.cytoscape.equations.internal.functions;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.equations.EquationCompiler;
import org.cytoscape.equations.Equation;
import org.cytoscape.equations.IdentDescriptor;
import org.cytoscape.equations.Interpreter;
import org.cytoscape.equations.internal.EquationCompilerImpl;
import org.cytoscape.equations.internal.EquationParserImpl;
import org.cytoscape.equations.internal.SUIDToNodeMapper;
import org.cytoscape.equations.internal.interpreter.InterpreterImpl;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.events.AddedNodesEvent;
import org.junit.Before;
import org.junit.Test;


public class DegreeTest {
	private SUIDToNodeMapper suidToNodeMapper;
	private CyApplicationManager applicationManager;

	@Before
	public void init() {
		final CyNode node = mock(CyNode.class);
		when(node.getSUID()).thenReturn(101L);

		final List<CyEdge> edgeList = mock(List.class);
		when(edgeList.size()).thenReturn(3);

		final CyNetwork network = mock(CyNetwork.class);
		when(network.getAdjacentEdgeList(node, CyEdge.Type.ANY)).thenReturn(edgeList);

		Collection<CyNode> nodes = new ArrayList<CyNode>(1);
		nodes.add(node);
		
		suidToNodeMapper = new SUIDToNodeMapper();
		suidToNodeMapper.handleEvent(new AddedNodesEvent(network, nodes));

		applicationManager = mock(CyApplicationManager.class);
		when(applicationManager.getCurrentNetwork()).thenReturn(network);
	}

	@Test
	public void test() {
		final EquationCompiler compiler = new EquationCompilerImpl(new EquationParserImpl());
		compiler.getParser().registerFunction(new Degree(applicationManager, suidToNodeMapper));
		final Map<String, Class<?>> variableNameToTypeMap = new HashMap<String, Class<?>>();
		if (!compiler.compile("=DEGREE(101)", variableNameToTypeMap))
			fail(compiler.getLastErrorMsg());
		final Equation equation = compiler.getEquation();
		final Interpreter interpreter = new InterpreterImpl();
		final Map<String, IdentDescriptor> variableNameToDescriptorMap = new HashMap<String, IdentDescriptor>();
		assertEquals("Equation evaluation returned an unexpected result!", 3L,
			     interpreter.execute(equation, variableNameToDescriptorMap));
	}
}
