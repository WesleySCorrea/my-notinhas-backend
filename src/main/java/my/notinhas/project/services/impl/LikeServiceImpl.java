package my.notinhas.project.services.impl;

import lombok.AllArgsConstructor;
import my.notinhas.project.dtos.LikeDTO;
import my.notinhas.project.dtos.LikeResponseDTO;
import my.notinhas.project.entities.Likes;
import my.notinhas.project.repositories.LikeRepository;
import my.notinhas.project.services.LikeService;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository repository;
    private final ModelMapper mapper;

    @Override
    public List<LikeResponseDTO> findAll() {

        List<Likes> likes = this.repository.findAll();

        return  likes.stream()
                .map(like -> mapper.map(like, LikeResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public LikeResponseDTO findByID(Long id) {

        Likes like = this.repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Like with ID " + id + " not found."));

        return mapper.map(like, LikeResponseDTO.class);
    }

    @Override
    public LikeDTO saveLike(LikeDTO likeDTO) {

        Likes request = mapper.map(likeDTO, Likes.class);

        Likes newLike = this.repository.save(request);

        return mapper.map(newLike, LikeDTO.class);
    }

    @Override
    public LikeDTO updateLike(LikeDTO likeDTO, Long id) {

        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        var likePersisted = this.findByID(id);
        mapper.map(likeDTO, likePersisted);
        LikeDTO likeToPersist = mapper.map(likePersisted, LikeDTO.class);

        return this.saveLike(mapper.map(likeToPersist, LikeDTO.class));
    }

    @Override
    public void deleteByID(Long id) {

        LikeResponseDTO like = this.findByID(id);

        if (like!=null) {
            this.repository.deleteById(id);
        } else {
            throw new NoSuchElementException("Like with ID: " + id + " not found!");
        }
    }
}
