package cn.maple.core.framework.mapstruct.res;

import cn.maple.core.framework.dto.inner.res.GXPaginationResDto;
import cn.maple.core.framework.dto.protocol.res.GXPaginationProtocol;
import cn.maple.core.framework.mapstruct.GXBaseMapStruct;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GXPaginationResMapStruct<R> extends GXBaseMapStruct<GXPaginationResDto<R>, GXPaginationProtocol<R>> {
}
