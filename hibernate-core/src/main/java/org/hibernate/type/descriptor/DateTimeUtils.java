/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.type.descriptor;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import java.util.TimeZone;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;

/**
 * Utilities for dealing with date/times
 *
 * @author Steve Ebersole
 * @author Gavin King
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public final class DateTimeUtils {
	private DateTimeUtils() {
	}

	public static final String FORMAT_STRING_DATE = "yyyy-MM-dd";
	public static final String FORMAT_STRING_TIME = "HH:mm:ss";
	public static final String FORMAT_STRING_TIMESTAMP = "yyyy-MM-dd HH:mm:ss.SSSSSS";
	public static final String FORMAT_STRING_TIMESTAMP_WITH_OFFSET = "yyyy-MM-dd HH:mm:ss.SSSSSSxxx";

	public static final DateTimeFormatter DATE_TIME_FORMATTER_DATE = DateTimeFormatter.ofPattern( FORMAT_STRING_DATE, Locale.ENGLISH );
	public static final DateTimeFormatter DATE_TIME_FORMATTER_TIME = DateTimeFormatter.ofPattern( FORMAT_STRING_TIME, Locale.ENGLISH );
	public static final DateTimeFormatter DATE_TIME_FORMATTER_TIMESTAMP = DateTimeFormatter.ofPattern( FORMAT_STRING_TIMESTAMP, Locale.ENGLISH );
	public static final DateTimeFormatter DATE_TIME_FORMATTER_TIMESTAMP_WITH_OFFSET = DateTimeFormatter.ofPattern( FORMAT_STRING_TIMESTAMP_WITH_OFFSET, Locale.ENGLISH );

	public static final String JDBC_ESCAPE_START_DATE = "{d '";
	public static final String JDBC_ESCAPE_START_TIME = "{t '";
	public static final String JDBC_ESCAPE_START_TIMESTAMP = "{ts '";
	public static final String JDBC_ESCAPE_END = "'}";

	/**
	 * Pattern used for parsing literal datetimes in HQL.
	 *
	 * Recognizes timestamps consisting of a date and time separated
	 * by either T or a space, and with an optional offset or time
	 * zone ID. Ideally we should accept both ISO and SQL standard
	 * zoned timestamp formats here.
	 */
	public static final DateTimeFormatter DATE_TIME = new DateTimeFormatterBuilder()
			.parseCaseInsensitive()
			.append( ISO_LOCAL_DATE )
			.optionalStart().appendLiteral( ' ' ).optionalEnd()
			.optionalStart().appendLiteral( 'T' ).optionalEnd()
			.append( ISO_LOCAL_TIME )
			.optionalStart().appendLiteral( ' ' ).optionalEnd()
			.optionalStart().appendZoneOrOffsetId().optionalEnd()
			.toFormatter();

	/**
	 * Pattern used for parsing literal offset datetimes in HQL.
	 *
	 * Recognizes timestamps consisting of a date and time separated
	 * by either T or a space, and with a required offset. Ideally we
	 * should accept both ISO and SQL standard timestamp formats here.
	 */
	public static final DateTimeFormatter OFFSET_DATE_TIME = new DateTimeFormatterBuilder()
			.parseCaseInsensitive()
			.append( ISO_LOCAL_DATE )
			.optionalStart().appendLiteral( ' ' ).optionalEnd()
			.optionalStart().appendLiteral( 'T' ).optionalEnd()
			.append( ISO_LOCAL_TIME )
			.optionalStart().appendLiteral( ' ' ).optionalEnd()
			.appendOffset("+HH:mm", "+00")
			.toFormatter();

	public static String formatAsTimestamp(TemporalAccessor temporalAccessor) {
		return temporalAccessor.isSupported(ChronoField.OFFSET_SECONDS)
				? DATE_TIME_FORMATTER_TIMESTAMP_WITH_OFFSET.format( temporalAccessor )
				: DATE_TIME_FORMATTER_TIMESTAMP.format( temporalAccessor );
	}

	public static String formatAsDate(TemporalAccessor temporalAccessor) {
		return DATE_TIME_FORMATTER_DATE.format( temporalAccessor );
	}

	public static String formatAsTime(TemporalAccessor temporalAccessor) {
		return DATE_TIME_FORMATTER_TIME.format( temporalAccessor );
	}

	public static String formatAsTimestamp(java.util.Date date) {
		return simpleDateFormatTimestamp().format( date );
	}

	public static String wrapAsJdbcDateLiteral(String literal) {
		return JDBC_ESCAPE_START_DATE + literal + JDBC_ESCAPE_END;
	}

	public static String wrapAsJdbcTimeLiteral(String literal) {
		return JDBC_ESCAPE_START_TIME + literal + JDBC_ESCAPE_END;
	}

	public static String wrapAsJdbcTimestampLiteral(String literal) {
		return JDBC_ESCAPE_START_TIMESTAMP + literal + JDBC_ESCAPE_END;
	}

	public static String wrapAsAnsiDateLiteral(String literal) {
		return "date '" + literal + "'";
	}

	public static String wrapAsAnsiTimeLiteral(String literal) {
		return "time '" + literal + "'";
	}

	public static String wrapAsAnsiTimestampLiteral(String literal) {
		return "timestamp '" + literal + "'";
	}

	public static SimpleDateFormat simpleDateFormatTimestamp() {
		return new SimpleDateFormat( FORMAT_STRING_TIMESTAMP, Locale.ENGLISH );
	}

	public static String formatAsDate(java.util.Date date) {
		return simpleDateFormatDate().format( date );
	}

	public static SimpleDateFormat simpleDateFormatDate() {
		return new SimpleDateFormat( FORMAT_STRING_DATE, Locale.ENGLISH );
	}

	public static String formatAsTime(java.util.Date date) {
		return simpleDateFormatTime().format( date );
	}

	public static SimpleDateFormat simpleDateFormatTime() {
		return new SimpleDateFormat( FORMAT_STRING_TIME, Locale.ENGLISH );
	}

	public static String formatAsTimestamp(java.util.Calendar calendar) {
		return simpleDateFormatTimestamp( calendar.getTimeZone() ).format( calendar.getTime() );
	}

	public static SimpleDateFormat simpleDateFormatTimestamp(TimeZone timeZone) {
		final SimpleDateFormat formatter = new SimpleDateFormat( FORMAT_STRING_TIMESTAMP, Locale.ENGLISH );
		formatter.setTimeZone( timeZone );
		return formatter;
	}

	public static String formatAsDate(java.util.Calendar calendar) {
		return simpleDateFormatDate( calendar.getTimeZone() ).format( calendar.getTime() );
	}

	public static SimpleDateFormat simpleDateFormatDate(TimeZone timeZone) {
		final SimpleDateFormat formatter = new SimpleDateFormat( FORMAT_STRING_DATE, Locale.ENGLISH );
		formatter.setTimeZone( timeZone );
		return formatter;
	}

	public static String formatAsTime(java.util.Calendar calendar) {
		return simpleDateFormatTime( calendar.getTimeZone() ).format( calendar.getTime() );
	}

	public static SimpleDateFormat simpleDateFormatTime(TimeZone timeZone) {
		final SimpleDateFormat formatter = new SimpleDateFormat( FORMAT_STRING_TIME, Locale.ENGLISH );
		formatter.setTimeZone( timeZone );
		return formatter;
	}

}