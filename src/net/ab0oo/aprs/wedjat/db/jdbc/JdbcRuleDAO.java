/*
 * AVRS - http://avrs.sourceforge.net/
 *
 * Copyright (C) 2011 John Gorkos, AB0OO
 *
 * AVRS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * AVRS is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AVRS; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */
package net.ab0oo.aprs.wedjat.db.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.ab0oo.aprs.wedjat.db.RuleDAO;
import net.ab0oo.aprs.wedjat.models.Rule;

/**
 * @author johng
 *
 */
public class JdbcRuleDAO extends BaseJdbcDAO implements RuleDAO {

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjet.db.RuleDAO#getRule(java.lang.Long)
	 */
	@Override
	public Rule getRule(Long ruleId) {
		Connection dbconn = null;
		PreparedStatement selectRules = null;
		ResultSet rs = null;
		Rule rule = null;
		try {
			dbconn = ds.getConnection();
			selectRules = dbconn.prepareStatement("SELECT * from rules  where rule_id=?");
			selectRules.setLong(1, ruleId);
			rs = selectRules.executeQuery();
			while (rs.next()) {
					rule = new Rule();
					rule.setUserId(rs.getLong("user_id"));
					rule.setStationId(rs.getLong("station_id"));
					rule.setRuleId(rs.getLong("rule_id"));
					rule.setCycleTime(rs.getLong("cycle_time"));
					rule.setRuleType(rs.getString("ruletype"));
					rule.setZoneId(rs.getLong("zone_id"));
					rule.setNextEnabled(rs.getTimestamp("next_enabled"));
			}
		} catch (SQLException sqlex) {
			System.err.println("SQL Exception:  " + sqlex);
        } finally {
            try {
                dbconn.close();
            } catch ( Exception ex ) {}
		}
		return rule;
	}

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjet.db.RuleDAO#getRulesByStationId(java.lang.Long)
	 */
	@Override
	public List<Rule> getRulesByStationId(Long stationId) {
		Connection dbconn = null;
		PreparedStatement selectRules = null;
		ResultSet rs = null;
		List<Rule> rules = new ArrayList<Rule>();
		try {
			dbconn = ds.getConnection();
			selectRules = dbconn.prepareStatement("SELECT * from rules  where station_id=?");
			selectRules.setLong(1, stationId);
			rs = selectRules.executeQuery();
			while (rs.next()) {
					Rule rule = new Rule();
					rule.setUserId(rs.getLong("user_id"));
					rule.setStationId(rs.getLong("station_id"));
					rule.setRuleId(rs.getLong("rule_id"));
					rule.setZoneId(rs.getLong("zone_id"));
					rule.setCycleTime(rs.getLong("cycle_time"));
					rule.setRuleType(rs.getString("ruletype"));
					rule.setNextEnabled(rs.getTimestamp("next_enabled"));
					rules.add(rule);
			}
		} catch (SQLException sqlex) {
			System.err.println("SQL Exception:  " + sqlex);
        } finally {
            try {
                dbconn.close();
            } catch ( Exception ex ) {}
		}
		return rules;
	}

	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjet.db.RuleDAO#getRulesByStationId(java.lang.Long)
	 */
	@Override
	public List<Rule> getActiveRulesByStationId(Long stationId) {
		Connection dbconn = null;
		PreparedStatement selectRules = null;
		ResultSet rs = null;
		List<Rule> rules = new ArrayList<Rule>();
		try {
			dbconn = ds.getConnection();
			selectRules = dbconn.prepareStatement("SELECT * from rules  where station_id=? and next_enabled < now()");
			selectRules.setLong(1, stationId);
			rs = selectRules.executeQuery();
			while (rs.next()) {
					Rule rule = new Rule();
					rule.setUserId(rs.getLong("user_id"));
					rule.setStationId(rs.getLong("station_id"));
					rule.setRuleId(rs.getLong("rule_id"));
					rule.setZoneId(rs.getLong("zone_id"));
					rule.setCycleTime(rs.getLong("cycle_time"));
					rule.setRuleType(rs.getString("ruletype"));
					rule.setNextEnabled(rs.getTimestamp("next_enabled"));
					rules.add(rule);
			}
		} catch (SQLException sqlex) {
			System.err.println("SQL Exception:  " + sqlex);
        } finally {
            try {
                dbconn.close();
            } catch ( Exception ex ) {}
		}
		return rules;
	}
	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjet.db.RuleDAO#getRulesByUserId(java.lang.Long)
	 */
	@Override
	public List<Rule> getRulesByUserId(Long userId) {
		Connection dbconn = null;
		PreparedStatement selectRules = null;
		ResultSet rs = null;
		List<Rule> rules = new ArrayList<Rule>();
		try {
			dbconn = ds.getConnection();
			selectRules = dbconn.prepareStatement("SELECT * from rules  where user_id=?");
			selectRules.setLong(1, userId);
			rs = selectRules.executeQuery();
			while (rs.next()) {
					Rule rule = new Rule();
					rule.setUserId(rs.getLong("user_id"));
					rule.setStationId(rs.getLong("station_id"));
					rule.setRuleId(rs.getLong("rule_id"));
					rule.setZoneId(rs.getLong("zone_id"));
					rule.setCycleTime(rs.getLong("cycle_time"));
					rule.setRuleType(rs.getString("ruletype"));
					rule.setNextEnabled(rs.getTimestamp("next_enabled"));
					rules.add(rule);
			}
		} catch (SQLException sqlex) {
			System.err.println("SQL Exception:  " + sqlex);
        } finally {
            try {
                dbconn.close();
            } catch ( Exception ex ) {}
		}
		return rules;
	}

	@Override
    public int resetRuleTimer(Long ruleId, Date newTime) {
		Connection dbconn = null;
		PreparedStatement updateRule = null;
		int resultRows = 0;
		try {
			dbconn = ds.getConnection();
			updateRule = dbconn.prepareStatement("UPDATE rules set next_enabled=? where rule_id=?");
			java.sql.Timestamp newSqlTime = new java.sql.Timestamp(newTime.getTime());
			updateRule.setTimestamp(1, newSqlTime);
			updateRule.setLong(2,ruleId);
			resultRows = updateRule.executeUpdate(); 
		} catch (SQLException sqlex) {
			System.err.println("SQL Exception:  " + sqlex);
        } finally {
            try {
                dbconn.close();
            } catch ( Exception ex ) {}
		}
		return resultRows;
	}
	
	@Override
    public int resetRuleTimer(Long ruleId) {
		Connection dbconn = null;
		PreparedStatement updateRule = null;
		int resultRows = 0;
		try {
			dbconn = ds.getConnection();
			// kudos to raptelan on the Freenode IRC channel #postgresql for the 
			// cycle_time * interval '1 minute' trick. 
			updateRule = dbconn.prepareStatement(
					"UPDATE rules SET next_enabled = now() + cycle_time * interval '1 minutes' where rule_id=?");
			updateRule.setLong(1,ruleId);
			resultRows = updateRule.executeUpdate(); 
		} catch (SQLException sqlex) {
			System.err.println("SQL Exception:  " + sqlex);
        } finally {
            try {
                dbconn.close();
            } catch ( Exception ex ) {}
		}
		return resultRows;
	}
}
