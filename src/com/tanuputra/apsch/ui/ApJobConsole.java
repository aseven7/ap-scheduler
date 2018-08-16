package com.tanuputra.apsch.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tanuputra.apsch.core.ApJobManager;
import com.tanuputra.apsch.jabx.ApJob;
import com.tanuputra.apsch.jabx.ApJobXml;
import com.tanuputra.apsch.jabx.ApXml;
import com.tanuputra.apsch.util.ApUtil;

public class ApJobConsole extends JFrame {
	private static final long serialVersionUID = 1L;
	private final static Logger _logger = LogManager.getLogger();
	private JTable _jobTableGrid;
	private JTable _jobGroupTableGrid;
	private DefaultTableModel _defaultTableModel;
	private Properties _apProp;

	public ApJobConsole() {
		_apProp = ApUtil.getApProp();

		loadUI();
		loadJobList();
	}

	public void loadJobList() {
		// Table header
		final String columnString = _apProp.getProperty("ap.console.columns");
		String[] columns = columnString.split(",");
		_defaultTableModel.setColumnIdentifiers(columns);

		// Table record
		final ApXml apXml = ApUtil.getJobXML(_logger, _apProp);
		for (ApJobXml xml : apXml.jobs.getJob()) {
			Vector<String> record = new Vector<String>();

			for (String columnName : columns) {
				try {
					Method method = ApJobXml.class.getMethod(ApUtil.getMethodGet(columnName));
					final Object returnValue = method.invoke(xml);
					record.add(returnValue.toString());
				} catch (Exception e) {
					_logger.error(e.getMessage());
				}
			}

			_defaultTableModel.addRow(record);
		}

		_defaultTableModel.fireTableStructureChanged();
		_jobTableGrid.validate();

	}
	

	public void loadJobGroupList() {
		// Table header
		final String columnString = _apProp.getProperty("ap.console.columns");
		String[] columns = columnString.split(",");
		_defaultTableModel.setColumnIdentifiers(columns);

		// Table record
		final ApXml apXml = ApUtil.getJobXML(_logger, _apProp);
		for (ApJobXml xml : apXml.jobs.getJob()) {
			Vector<String> record = new Vector<String>();

			for (String columnName : columns) {
				try {
					Method method = ApJobXml.class.getMethod(ApUtil.getMethodGet(columnName));
					final Object returnValue = method.invoke(xml);
					record.add(returnValue.toString());
				} catch (Exception e) {
					_logger.error(e.getMessage());
				}
			}

			_defaultTableModel.addRow(record);
		}

		_defaultTableModel.fireTableStructureChanged();
		_jobTableGrid.validate();

	}

	public void loadUI() {
		_defaultTableModel = new DefaultTableModel();
		_jobTableGrid = new JTable(_defaultTableModel);

		final Dimension screenDimension = new Dimension(1000, 500);

		/** props */
		this.setLayout(new BorderLayout());
		this.setTitle("AP Job Console");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(screenDimension);
		this.setVisible(true);
		

		this.getContentPane().add(new JScrollPane(_jobTableGrid, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS), BorderLayout.NORTH);

		this.pack();
		this.setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		new ApJobConsole();
	}

}
