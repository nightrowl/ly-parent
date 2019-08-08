package com.ly.item.mapper;

import com.ly.item.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 *
 * @author 70719
 */
public interface BrandMapper extends Mapper<Brand>,IdListMapper<Brand,Long>,InsertListMapper<Brand> {

    @Insert("insert into tb_category_brand values (#{cid} , #{bid})")
    int insertCategoryBrand(@Param("cid") Long cid, @Param("bid") Long bid);

    @Delete("delete from tb_category_brand where brand_id = #{bid}")
    int deleteCategoryBrand(@Param("bid") Long bid) ;

    @Select("select t.* from tb_brand t INNER join tb_category_brand tb on  t.id = tb.brand_id\n" +
            "where tb.category_id = #{cid}")
    List<Brand> queryByCId(@Param("cid") Integer cid);
}
