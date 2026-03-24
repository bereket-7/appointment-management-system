package com.beki.appointment.interfaces.mapper;

import com.beki.appointment.domain.appointment.Appointment;
import com.beki.appointment.interfaces.dto.AppointmentDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AppointmentMapper {

    public AppointmentDto toDto(Appointment appointment) {
        if (appointment == null) {
            return null;
        }

        AppointmentDto dto = new AppointmentDto();
        dto.setAppointmentId(appointment.getAppointmentId());
        dto.setUserId(appointment.getUserId());
        dto.setProviderId(appointment.getProviderId());
        dto.setAppointmentDate(appointment.getAppointmentDate());
        dto.setStatus(appointment.getStatus());
        dto.setNotes(appointment.getNotes());
        dto.setCreationDate(appointment.getCreationDate());
        dto.setLastUpdateDate(appointment.getLastUpdateDate());

        return dto;
    }

    public Appointment toEntity(AppointmentDto dto) {
        if (dto == null) {
            return null;
        }

        Appointment appointment = new Appointment();
        appointment.setUserId(dto.getUserId());
        appointment.setProviderId(dto.getProviderId());
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setStatus(dto.getStatus());
        appointment.setNotes(dto.getNotes());

        return appointment;
    }

    public void updateEntity(Appointment appointment, AppointmentDto dto) {
        if (dto == null || appointment == null) {
            return;
        }

        if (dto.getUserId() != null) {
            appointment.setUserId(dto.getUserId());
        }
        if (dto.getProviderId() != null) {
            appointment.setProviderId(dto.getProviderId());
        }
        if (dto.getAppointmentDate() != null) {
            appointment.setAppointmentDate(dto.getAppointmentDate());
        }
        if (dto.getStatus() != null) {
            appointment.setStatus(dto.getStatus());
        }
        if (dto.getNotes() != null) {
            appointment.setNotes(dto.getNotes());
        }
    }

    public List<AppointmentDto> toDtoList(List<Appointment> appointments) {
        return appointments.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<Appointment> toEntityList(List<AppointmentDto> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
