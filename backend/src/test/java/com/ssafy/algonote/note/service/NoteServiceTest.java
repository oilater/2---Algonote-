package com.ssafy.algonote.note.service;

import com.ssafy.algonote.exception.CustomException;
import com.ssafy.algonote.member.domain.Member;
import com.ssafy.algonote.member.repository.MemberRepository;
import com.ssafy.algonote.note.domain.Note;
import com.ssafy.algonote.note.repository.NoteRepository;
import com.ssafy.algonote.problem.domain.Problem;
import com.ssafy.algonote.problem.repository.ProblemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @InjectMocks
    private NoteService sut;
    @Mock
    private NoteRepository noteRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private ProblemRepository problemRepository;

    // 노트 생성
    @Test
    @DisplayName("[생성] 노트 작성이 성공한 경우")
    void givenNoteData_whenSaveNote_thenNothing() {
        //given
        String title = "title";
        String content = "content";
        Long memberId = 1L;
        Long problemId = 1000L;

        //mocking
        given(memberRepository.findById(memberId)).willReturn(Optional.of(mock(Member.class)));
        given(noteRepository.save(any())).willReturn(mock(Note.class));
        given(problemRepository.findById(problemId)).willReturn(Optional.of(mock(Problem.class)));

        //when
        sut.saveNote(memberId, problemId, title, content);

        //then
        verify(noteRepository).save(any());
    }

    @Test
    @DisplayName("[생성] 노트 작성 시 요청한 유저가 존재하지 않는 경우")
    void givenNoteDataWithoutMember_whenSaveNote_thenThrowException() {
        // given
        String title = "title";
        String content = "content";
        Long memberId = 1L;
        Long problemId = 1000L;

        given(memberRepository.findById(memberId)).willReturn(Optional.empty());

        //when & then
        assertThrows(CustomException.class, () -> {
            sut.saveNote(memberId, problemId, title, content);
        });
    }

    @Test
    @DisplayName("[생성] 노트 작성 시 존재하지 않는 문제를 등록한 경우")
    void test() {
        // given
        String title = "title";
        String content = "content";
        Long memberId = 1L;
        Long problemId = 1000L;

        given(memberRepository.findById(memberId)).willReturn(Optional.of(mock(Member.class)));
        given(problemRepository.findById(problemId)).willReturn(Optional.empty());

        // when & then
        assertThrows(CustomException.class, () -> {
            sut.saveNote(memberId, problemId, title, content);
        });
    }

    //TODO : [생성] 푼 적이 없는 문제를 오답노트로 작성하려고 했을 때

    // 노트 삭제
    @Test
    @DisplayName("[삭제] 내가 작성한 노트 삭제")
    void givenUserId_whenDeleteNote_thenNothing() {
        Long memberId = 1L;
        Long noteId = 1000L;
        Member member = createMember(memberId);
        Note note = createNote(noteId, member);

        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(noteRepository.findById(noteId)).willReturn(Optional.of(note));

        sut.deleteNote(memberId, noteId);
        verify(noteRepository).delete(any());
    }


    // 노트 조회



    // 노트 업데이트

    private Member createMember(Long memberId) {
        Member member = new Member();
        ReflectionTestUtils.setField(member, "id", memberId);
        return member;
    }
    private Note createNote(Long noteId, Member member) {
        Note note = new Note();
        ReflectionTestUtils.setField(note, "id", noteId);
        ReflectionTestUtils.setField(note, "member", member);
        return note;
    }
}