package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.model.compilation.Compilation;
import ru.practicum.model.compilation.dto.CompilationDto;
import ru.practicum.model.compilation.dto.NewCompilationDto;
import ru.practicum.model.event.Event;

import java.util.Set;

@Mapper(componentModel = "spring", uses = {EventMapper.class})
public interface CompilationMapper {
    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "events", expression = "java(events)")
    Compilation toCompilation(NewCompilationDto newCompilationDto, Set<Event> events);

    CompilationDto toCompilationDto(Compilation compilation);
}
