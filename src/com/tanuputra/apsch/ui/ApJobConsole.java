package com.tanuputra.apsch.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tanuputra.apsch.jabx.ApJobGroupXml;
import com.tanuputra.apsch.jabx.ApJobXml;
import com.tanuputra.apsch.jabx.ApXml;
import com.tanuputra.apsch.util.ApUtil;

public class ApJobConsole extends JFrame {
	private static final long serialVersionUID = 1L;
	private final static Logger _logger = LogManager.getLogger();
	private JTable _jobTableGrid;
	private JTable _jobGroupTableGrid;
	private Properties _apProp;

	public ApJobConsole() {
		_apProp = ApUtil.getApProp();

		loadUI();
		loadJobList();
		loadJobGroupList();
	}

	public void loadJobList() {
		DefaultTableModel defaultTableModel = new DefaultTableModel();

		// Table header
		final String columnString = _apProp.getProperty("ap.console.columns");
		String[] columns = columnString.split(",");
		defaultTableModel.setColumnIdentifiers(columns);

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

			defaultTableModel.addRow(record);
		}
		
		resizeColumnWidth(_jobTableGrid);
		defaultTableModel.fireTableStructureChanged();
		_jobTableGrid.setModel(defaultTableModel);
		_jobTableGrid.validate();

	}

	public void loadJobGroupList() {
		DefaultTableModel defaultTableModel = new DefaultTableModel();

		// Table header
		final String columnString = _apProp.getProperty("ap.console.columngroups");
		String[] columns = columnString.split(",");
		defaultTableModel.setColumnIdentifiers(columns);

		// Table record
		final ApXml apXml = ApUtil.getJobXML(_logger, _apProp);
		for (ApJobGroupXml xml : apXml.jobGroups.getJobGroup()) {
			Vector<String> record = new Vector<String>();

			for (String columnName : columns) {
				try {
					Method method = ApJobGroupXml.class.getMethod(ApUtil.getMethodGet(columnName));
					final Object returnValue = method.invoke(xml);
					record.add(returnValue.toString());
				} catch (Exception e) {
					_logger.error(e.getMessage());
				}
			}

			defaultTableModel.addRow(record);
		}

		defaultTableModel.fireTableStructureChanged();
		_jobGroupTableGrid.setModel(defaultTableModel);
		_jobGroupTableGrid.validate();

	}

	public void loadUI() {
		_jobTableGrid = new JTable();
		_jobTableGrid.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		_jobGroupTableGrid = new JTable();
		_jobGroupTableGrid.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		final Dimension screenDimension = new Dimension(1000, 500);

		/** props */
		this.setLayout(new BorderLayout());
		this.setTitle("AP Job Console");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(screenDimension);
		this.setVisible(true);

		this.getContentPane().add(new JScrollPane(_jobTableGrid, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS), BorderLayout.EAST);

		this.getContentPane().add(new JScrollPane(_jobGroupTableGrid, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS), BorderLayout.WEST);
		this.pack();
		this.setLocationRelativeTo(null);
	}

	public void resizeColumnWidth(JTable table) {
		final TableColumnModel columnModel = table.getColumnModel();
		for (int column = 0; column < table.getColumnCount(); column++) {
			int width = 15; // Min width
			for (int row = 0; row < table.getRowCount(); row++) {
				TableCellRenderer renderer = table.getCellRenderer(row, column);
				Component comp = table.prepareRenderer(renderer, row, column);
				width = Math.max(comp.getPreferredSize().width + 1, width);
			}
			if (width > 300)
				width = 300;
			columnModel.getColumn(column).setPreferredWidth(width);
		}
	}

	public static void main(String[] args) {
		new ApJobConsole();
	}

}
