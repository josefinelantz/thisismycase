package se.lantz.thisismycase.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

/*
Kanske inte helt optimalt att ha setId() här, borde ha löst id-generering snyggare,
men insåg detta lite sent. Anledningen är att jag inte lyckades få PUT att funka
som update, den gjorde POST. Detta skulle ju kunnat fixas genom att generera
id-n i servicen, men jag valde att inte ändra detta när jag hade fått allt att funka.
 */

@MappedSuperclass
public abstract class AbstractEntity
{
	@Id
	@GeneratedValue
	private Long id;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEntityId()
	{
		return UUID.randomUUID().toString().substring(0, 4);
	}

	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
