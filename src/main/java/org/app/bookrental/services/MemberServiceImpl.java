package org.app.bookrental.services;

import org.app.bookrental.data.models.Member;
import org.app.bookrental.data.repositories.MemberRepository;
import org.app.bookrental.dtos.requests.AddMemberRequest;
import org.app.bookrental.dtos.requests.LoginRequest;
import org.app.bookrental.dtos.responses.AddMemberResponse;
import org.app.bookrental.dtos.responses.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberRepository memberRepository;


    @Override
    public Member findMemberByEmail(String emailAddy) {
        return memberRepository.findByEmail(emailAddy);
    }

    @Override
    public Member emailAlreadyExists( AddMemberRequest addMemberRequest) {
        Member getMember = findMemberByEmail(addMemberRequest.getEmail());
        if (getMember != null && getMember.getEmail().equals(addMemberRequest.getEmail())) {
            throw new IllegalArgumentException("Email is already taken");
        }
        return getMember;
    }

    @Override
    public AddMemberResponse emailCannotBeEmpty(AddMemberRequest addMemberRequest) {
        //Member getMember = findMemberByEmail(addMemberRequest.getEmail());
        if (addMemberRequest.getEmail() == null) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        return null;
    }


    @Override
    public AddMemberResponse registerMember(AddMemberRequest addMemberRequest) {
        Member getMember = findMemberByEmail(addMemberRequest.getEmail());
        if(getMember == null) {
            Member member = new Member();
            member.setFullName(addMemberRequest.getFullName());
            member.setEmail(addMemberRequest.getEmail());
            member.setPassword(addMemberRequest.getPassword());
            member.setPhoneNumber(addMemberRequest.getPhoneNumber());
            member.setAddress(addMemberRequest.getAddress());
            memberRepository.save(member);
        }
        AddMemberResponse regResponse = new AddMemberResponse();
        regResponse.setRegMsg("Registration successful");
        return regResponse;
    }

    @Override
    public Member loginEmail(LoginRequest loginRequest) {
        Member foundMember = findMemberByEmail(loginRequest.getEmail());
        if (foundMember == null) {
            throw new IllegalArgumentException("Cannot find email");
        }
        return foundMember;
    }

    @Override
    public Member loginPassword(LoginRequest loginRequest) {
        Member memberPassword = findMemberByEmail(loginRequest.getEmail());
        if (memberPassword == null || ! memberPassword.getPassword().equals(loginRequest.getPassword())) {
            throw new IllegalArgumentException("You have entered a wrong password");
        }
        return memberPassword;
    }

    @Override
    public Member loginMember(LoginRequest loginRequest) {
        Member foundMember = findMemberByEmail(loginRequest.getEmail());
        if (foundMember != null && foundMember.getPassword().equals(loginRequest.getPassword())) {
            return foundMember;
        } else {
            throw new IllegalArgumentException("Wrong password");
        }
    }




}